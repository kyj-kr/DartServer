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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);

		LocalTime localTime = LocalTime.now();
		localTime = localTime.minusMinutes(2);
		startTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));

		while(true) {
			// DB에서 유저들 정보 싹 긁어오기
			getUserDatas();

			// 최근 공시 회사 홈페이지에서 긁어오기
			getRecentCorps();

			// 최근 공시 올라온 회사랑 유저들 corpnames contains 여부 확인하고 존재하면 cloudmessage 보내기
			// notiVos에 notiVo를 넣을건데, 이는 알림 중복을 방지하기 위함
			// 자세히 말하자면, notiVo에 회사명이랑 보고서 제출번호가 필수적으로 들어가서 서버가 돌아가는 동안 계속 저장되어 있어서 똑같은 보고서에 절대 중복하여 알림을 보내지 않음
			// 그래서 notiVos에서 특정 notiVo를 remove 하는 코드는 추가하지 않음
			for (UserVo userVo : userDatas) {
				for (RecentCorpVo recentCorpVo : recentCorps) {
					if (userVo.getCorpNames().contains(recentCorpVo.getCorpName()) && !userVo.isContains(recentCorpVo.getReceptNum())) {
						userVo.getNotiVos().add(new NotiVo(recentCorpVo.getCorpName(), recentCorpVo.getTime(), recentCorpVo.getReceptNum(), false));
					}
				}

				String corpMsg = "";
				for(NotiVo notiVo : userVo.getNotiVos()) {
					if(!notiVo.isMessaged()) {
						notiVo.setMessaged(true);
						if(!corpMsg.contains(notiVo.getCorpName())) {
							if(corpMsg.equals("")) {
								corpMsg = notiVo.getCorpName();
							}
							else {
								corpMsg = corpMsg + ", " + notiVo.getCorpName();
							}
						}
					}
				}

				if(!corpMsg.equals("")) {
					try {
						new FirebaseCloudMessageService().sendMessageTo(userVo.getDeviceToken(), "공시 알림", corpMsg + "에서 공시가 올라왔어요!");
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}

			System.out.println("loop");
		}


	}

	private static void getUserDatas() {
		try {
			Request request = new Request.Builder()
					.url(FIREBASE_DB_URL + ".json")
					.build();

			Response response = client.newCall(request).execute();

			String responseString = response.body().string();
			parseUserData(responseString);

		} catch(Exception e) {
			e.printStackTrace();
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
				if(!isOpenQuote) { // 하나의 word를 모았을때
					switch(j % 5) {
						case 0: // androidId
							isPass = false;
							for(UserVo userVo : userDatas) {
								if(userVo.getAndroidId().equals(word)) { // 똑같은 androidId 있으면 패스
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
			if(isOpenQuote) { // quote 안의 인덱스일 경우
				word = word + ch;
			}
		}
	}

	private static void getRecentCorps() {
		try {
			// recentCorps 초기화
			recentCorps.clear();
			// 전체
			Request request = new Request.Builder()
					.url("http://dart.fss.or.kr/dsac001/mainAll.do")
					.build();

			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			parseRecentCorp(responseString);

			// 5퍼 임원
			request = new Request.Builder()
					.url("http://dart.fss.or.kr/dsac001/mainO.do")
					.build();

			response = client.newCall(request).execute();
			responseString = response.body().string();
			parseRecentCorp(responseString);

			// 펀드
			request = new Request.Builder()
					.url("http://dart.fss.or.kr/dsac001/mainF.do")
					.build();

			response = client.newCall(request).execute();
			responseString = response.body().string();
			parseRecentCorp(responseString);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void parseRecentCorp(String html) {
		RecentCorpVo recentCorpVo = null;
		String time = null;
		String corpName = null;
		String receptNum = null;

		Document document = Jsoup.parse(html);
		Elements elements = document.select("div[id=listContents] tbody tr td");

		int i = 0;
		boolean isNeed = false;
		for(Element element : elements) {
			switch(i % 6) {
				case 0:
					time = element.text();
					if(compareString(time, startTime) == 1) {
						recentCorpVo = new RecentCorpVo();
						recentCorpVo.setTime(time);
						isNeed = true;
					}
					break;

				case 1:
					if(isNeed) {
						corpName = element.select("a").first().text();
						recentCorpVo.setCorpName(corpName);
					}
					break;

				case 2:
					if(isNeed) {
						receptNum = element.select("a").attr("id").split("_")[1];
						recentCorpVo.setReceptNum(receptNum);
						recentCorps.add(recentCorpVo);
					}
					isNeed = false;
					break;

				default:
			}
			i++;
		}

	}

	/*
	* str1, str2를 비교
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
