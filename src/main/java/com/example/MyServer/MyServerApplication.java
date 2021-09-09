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

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);

		while(true) {
			// DB에서 유저들 정보 싹 긁어오기
			getUserDatas();

			// 최근 공시 회사 홈페이지에서 긁어오기
			getRecentCorps();

			// 최근 공시 올라온 회사랑 유저들 corpnames contains 여부 확인하고 존재하면 cloudmessage 보내기
			for (UserVo userVo : userDatas) {
				for (RecentCorpVo recentCorpVo : recentCorps) {
					if (userVo.getCorpNames().contains(recentCorpVo.getCorpName()) && !userVo.isContains(recentCorpVo.getReceptNum())) {
						userVo.getNotiVos().add(new NotiVo(recentCorpVo.getReceptNum(), false));
					}
				}

				for(NotiVo notiVo : userVo.getNotiVos()) {
					if(!notiVo.isMessaged()) {
						try {
							notiVo.setMessaged(true);
							new FirebaseCloudMessageService().sendMessageTo(userVo.getDeviceToken(), "공시 알림", "알림 설정된 공시가 올라왔어요!");
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			}

			System.out.println("End");
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
									break;
								}
							}
							if(isPass) break;
							userData = new UserVo();
							userData.setAndroidId(word);
							break;

						case 2: // corpNames
							if(isPass) break;
							userData.setCorpNames(word);
							break;

						case 4: // deviceToken
							if(isPass) break;
							userData.setDeviceToken(word);
							userDatas.add(userData);
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
			resultList.addAll(parseRecentCorp(responseString));

			// 펀드
			request = new Request.Builder()
					.url("http://dart.fss.or.kr/dsac001/mainF.do")
					.build();

			response = client.newCall(request).execute();
			responseString = response.body().string();
			resultList.addAll(parseRecentCorp(responseString));

			return resultList;

		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<RecentCorpVo> parseRecentCorp(String html) {
		ArrayList<RecentCorpVo> recentCorps = new ArrayList<>();
		RecentCorpVo recentCorpVo = null;
		String corpName = null;
		String receptNum = null;

		Document document = Jsoup.parse(html);
		Elements elements = document.select("div[id=listContents] tbody tr td[class=tL]");


		int i = 0;
		for(Element element : elements) {
			switch(i % 2) {
				case 0:
					recentCorpVo = new RecentCorpVo();
					corpName = element.select("a").text();
					recentCorpVo.setCorpName(corpName);
					break;

				case 1:
					receptNum = element.select("a").attr("id").split("_")[1];
					recentCorpVo.setReceptNum(receptNum);
					recentCorps.add(recentCorpVo);
					break;

				default:
					System.out.println("MyServerApplication Switch Error");
			}
			i++;
		}

		return recentCorps;
	}
}
