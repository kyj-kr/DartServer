package com.example.MyServer;

import com.example.MyServer.domain.NotiVo;
import com.example.MyServer.domain.RecentCorpVo;
import com.example.MyServer.domain.UserVo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

@SpringBootApplication
public class MyServerApplication {

//	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	private static final String URL = "jdbc:mysql://localhost:3306/201724433?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
//	private static final String USER = "root";
//	private static final String PASSWORD = "1234";

	private static OkHttpClient client = new OkHttpClient();
	private static final String FIREBASE_DB_URL = "https://dart-1f534-default-rtdb.firebaseio.com/users";
	private static ArrayList<UserVo> userDatas = new ArrayList<>();
	private static ArrayList<RecentCorpVo> recentCorps = new ArrayList<>();
	private static String startTime = "";

	public static HashMap<String, String[]> bussinessResultVisitedHistory = new HashMap<>();

	public static LocalTime msgTime = LocalTime.now();
	public static String prevTime;
	private static SendMsgService sendMsgService = new SendMsgService();

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);

		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();
		prevTime = msgTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		localTime = localTime.minusMinutes(2);
//		localTime = localTime.minusHours(2);
		startTime = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")) + localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		System.out.println("startTime: " + startTime);

		try {
			new FirebaseCloudMessageService().sendMessageTo("f8R8AziQRoucV0o6mjK_2a:APA91bGI74OJuPaB0Xyub19mjLCsFvz0EW8wNfiMwcy_qwXycienNs26khIhTUtjY2fSW4KpYDXksqTnK6U1jxBq7MO0Xrsr6fS8gXdwENXXmroOG5lRfelzewvlV5_96zs2PVtobey8", "??????????????????", "?????????????????? ???????????? ????????????", startTime, "20211028800526");
		} catch(Exception e) {

		}

//		String rate = new DisclosureRequestService().getRate("20211112801211");
//		System.out.println("rate: " + rate);

		getUserDatas();

		while (true) {

			if (isMsgTime(0) && isUpdateTime()) {
				sendMsgService.sendMySelf();
				System.out.println("send msg ok");
			}

			// DB?????? ????????? ?????? ??? ????????????
//			getUserDatas();

			// ?????? ?????? ?????? ?????????????????? ????????????
			getRecentCorps();

			try {

				// ?????? ?????? ????????? ????????? ????????? corpnames contains ?????? ???????????? ???????????? cloudmessage ?????????
				// notiVos??? notiVo??? ????????????, ?????? ?????? ????????? ???????????? ??????
				// ????????? ????????????, notiVo??? ??????????????? ????????? ??????????????? ??????????????? ???????????? ????????? ???????????? ?????? ?????? ???????????? ????????? ????????? ???????????? ?????? ???????????? ????????? ????????? ??????
				// ????????? notiVos?????? ?????? notiVo??? remove ?????? ????????? ???????????? ??????
				for (UserVo userVo : userDatas) {
					for (RecentCorpVo recentCorpVo : recentCorps) {
						if (isNewNoti(userVo, recentCorpVo.getReceptNum())) { // ?????? ?????? ???????????????

							if (isAlarmCorp(userVo.getCorpNames(), recentCorpVo.getCorpName())) { // ?????????????????????
								userVo.getNotiVos().add(new NotiVo(recentCorpVo.getCorpName(), recentCorpVo.getTime(), recentCorpVo.getReceptNum(), "", recentCorpVo.getTitle(), false));
							}

							if (isBusinessResultDisclosure(recentCorpVo.getTitle())) { // ?????????????????????
								String receptNum = recentCorpVo.getReceptNum();
								if (!isVisited(receptNum)) {
									String strRate[] = new DisclosureRequestService().getRate(receptNum);
									bussinessResultVisitedHistory.put(receptNum, strRate);
								}
								String rates[] = bussinessResultVisitedHistory.get(receptNum);
								String strRates = "";
								for (String rate : rates) {
									if (strRates.isEmpty()) {
										strRates = strRates + rate;
									} else {
										strRates = strRates + "&" + rate;
									}
								}
								userVo.getNotiVos().add(new NotiVo(recentCorpVo.getCorpName(), recentCorpVo.getTime(), recentCorpVo.getReceptNum(), strRates, recentCorpVo.getTitle(), false));
							}
						}
					}

					String corpInfoList = NotiVo.getCorpInfoList(userVo.getNotiVos());

					if (!corpInfoList.equals("")) {
						try {
							String[] corpInfos = corpInfoList.split(",");
							for (String corpInfo : corpInfos) {
								String corpName = corpInfo.split("/")[0];
								String corpDate = corpInfo.split("/")[1];
								String rates = corpInfo.split("/")[2];
								String title = corpInfo.split("/")[3];
								String receptNum = corpInfo.split("/")[4];
								String[] arrRates = rates.split("&");
								if (rates.equals("")) {
									new FirebaseCloudMessageService().sendMessageTo(userVo.getDeviceToken(), corpName, corpName + "?????? " + title + " ????????? ???????????????!", corpDate, receptNum);
								} else {
									new FirebaseCloudMessageService().sendMessageTo(userVo.getDeviceToken(), corpName, corpName + "?????? ?????? ??????????????? ????????? ???????????????!\n????????? ??????????????????????????? : " + arrRates[0] + "%\n????????? ????????????????????? : " + arrRates[1]
											+ "%\n???????????? ??????????????????????????? : " + arrRates[2] + "%\n???????????? ????????????????????? : " + arrRates[3] + "%\n????????? ??????????????????????????? : " + arrRates[4] + "%\n????????? ????????????????????? : " + arrRates[5] + "%", corpDate, receptNum);
								}
							}
						} catch (Exception e) {
							System.out.println("MyServerApplication 133: " + e.toString());
						}
					}

//					// test
//					if (userVo.getAndroidId().equals("2d6ab579f7e70086")) {
//						System.out.println(userVo.getCorpNames());
//					}
				}

				System.out.println("total user: " + userDatas.size());
			}

			catch(Exception e) {
				System.out.println("MyServerApplication 147: ");
				e.printStackTrace();
			}
		}


	}

	// ????????? ???????????????????????? minute ????????? ???????????? true ??????
	private static boolean isMsgTime(int minute) {
		msgTime = LocalTime.now();
		String nowTime = msgTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		msgTime = msgTime.minusMinutes(minute);
		String minusTime = msgTime.format(DateTimeFormatter.ofPattern("HH:mm"));

		if(compareString(minusTime, prevTime) == 1) {
			updatePrevTime(nowTime);
			return true;
		}
		return false;
	}

	private static void updatePrevTime(String time) {
		prevTime = time;
	}

	// 07~20??? ????????? true
	private static boolean isUpdateTime() {
		msgTime = LocalTime.now();
		System.out.println(msgTime);

		String nowTime = msgTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		if( (compareString(nowTime, "07:00") == 1) && (compareString("20:00", nowTime) == 1) ) {
			return true;
		}
		return false;
	}

	// ?????? ?????? ??????????????? true ??????
	private static boolean isNewNoti(UserVo userVo, String receptNum) {
		return !userVo.isContains(receptNum);
	}

	// ?????? ???????????????
	private static boolean isAlarmCorp(String userCorpName, String webCorpName) {
		return userCorpName.contains(webCorpName);
	}

	// ????????? ????????? ????????? ???????????? true ??????
	private static boolean isBusinessResultDisclosure(String title) {
		return title.contains("??????") && title.contains("??????");
	}

	private static boolean isVisited(String receptNum) {
		boolean result = false;
		for(String key : bussinessResultVisitedHistory.keySet()) {
			if(key.equals(receptNum)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static void getUserDatas() {
		try {
			Request request = new Request.Builder()
					.url(FIREBASE_DB_URL + ".json")
					.build();

			Response response = client.newCall(request).execute();

			String responseString = response.body().string();
			parseUserData(responseString);

		} catch(Exception e) {
			System.out.println("MyServerApplication 222: " + e.toString());
		}
	}

	private static void parseUserData(String rawData) {
		UserVo userData = null;

		int strSize = rawData.length();
		char ch;
		String word = "";
		boolean isOpenQuote = false;
		boolean isPass = false;
		int j = 0;
		for(int i = 0; i < strSize; i++) {
			ch = rawData.charAt(i);
			if(ch == '"') {
				isOpenQuote = !isOpenQuote;
				if(!isOpenQuote) { // ????????? word??? ????????????
					switch(j % 5) {
						case 0: // androidId
							isPass = false;
							for(UserVo userVo : userDatas) {
								if(userVo.getAndroidId().equals(word)) { // ????????? androidId ????????? ??????
									isPass = true;
									userData = userVo;
									break;
								}
							}
							if(!isPass) {
								userData = new UserVo();
								userData.setAndroidId(word);
							}
							break;

						case 2: // corpNames
							userData.setCorpNames(word);
							break;

						case 4: // deviceToken
							userData.setDeviceToken(word);
							if(!isPass) {
								userDatas.add(userData);
							}
							break;

						default:

					}
					word = "";
					j++;
				}
				continue;
			}
			if(isOpenQuote) { // quote ?????? ???????????? ??????
				word = word + ch;
			}
		}
	}

	private static void getRecentCorps() {
		try {
			// recentCorps ?????????
			recentCorps.clear();
			// ??????
			Request request = new Request.Builder()
					.url("https://dart.fss.or.kr/dsac001/mainAll.do")
					.build();

			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			parseRecentCorp(responseString);

			// 5??? ??????
			request = new Request.Builder()
					.url("https://dart.fss.or.kr/dsac001/mainO.do")
					.build();

			response = client.newCall(request).execute();
			responseString = response.body().string();
			parseRecentCorp(responseString);

			// ??????
			request = new Request.Builder()
					.url("https://dart.fss.or.kr/dsac001/mainF.do")
					.build();

			response = client.newCall(request).execute();
			responseString = response.body().string();
			parseRecentCorp(responseString);

		} catch(Exception e) {
			System.out.println("MyServerApplication 313: " + e.toString());
		}
	}

	private static void parseRecentCorp(String html) {
		RecentCorpVo recentCorpVo = null;
		String title = null;
		String time = null;
		String corpName = null;
		String receptNum = null;
		String dateAndTime = null;

		Document document = Jsoup.parse(html);
		Elements elements = document.select("div[id=listContents] tbody tr td");

		int i = 0;
		boolean needAlloc = true;
		for(Element element : elements) {
			switch(i % 6) {
				case 0:
					time = element.text();
					if(needAlloc) {
						recentCorpVo = new RecentCorpVo();
					}
//					recentCorpVo.setTime(time);
					break;

				case 1:
					corpName = element.select("a").first().text();
					recentCorpVo.setCorpName(corpName);
					break;

				case 2:
					receptNum = element.select("a").attr("id").split("_")[1];
					recentCorpVo.setReceptNum(receptNum);
					title = element.select("a").text();
					recentCorpVo.setTitle(title);
					break;

				case 4:
					dateAndTime = element.text() + "." + time;
					recentCorpVo.setTime(dateAndTime);
					if(compareString(dateAndTime, startTime) > 0) {
						recentCorps.add(recentCorpVo);
						needAlloc = true;
					}
					else {
						needAlloc = false;
					}
					break;


				default:
			}
			i++;
		}

	}

	/*
	 * str1, str2??? ??????
	 */
	public static int compareString(String str1, String str2) {
		for(int i = 0; i < str1.length(); i++) {
			char c1 = str1.charAt(i);
			char c2 = str2.charAt(i);

			if(c1 < c2) {
				return -1;
			}
			else if(c1 > c2) {
				return 1;
			}
		}
		return 0; // str1 == str2
	}
}
