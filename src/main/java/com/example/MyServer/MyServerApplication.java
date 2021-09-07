package com.example.MyServer;

import com.example.MyServer.domain.TokenVo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.Param;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

@SpringBootApplication
public class MyServerApplication {

//	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	private static final String URL = "jdbc:mysql://localhost:3306/201724433?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
//	private static final String USER = "root";
//	private static final String PASSWORD = "1234";

	private static final String FIREBASE_DB_URL = "https://dart-1f534-default-rtdb.firebaseio.com/users";

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);

		// DB에서 유저들 정보 싹 긁어오기
		ArrayList<TokenVo> userDatas = getUserDatas();
	}

	private static ArrayList<TokenVo> getUserDatas() {
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(FIREBASE_DB_URL + ".json")
					.build();

			Response response = client.newCall(request).execute();

			return parseRawData(response.body().string());

		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<TokenVo> parseRawData(String rawData) {
		ArrayList<TokenVo> userDatas = new ArrayList<>();
		TokenVo userData = null;

		int strSize = rawData.length();
		char ch;
		String word = "";
		boolean isOpenQuote = false;
		int j = 0;
		for(int i = 0; i < strSize; i++) {
			ch = rawData.charAt(i);
			if(ch == '"') {
				isOpenQuote = !isOpenQuote;
				if(!isOpenQuote) {
					switch(j % 5) {
						case 0: // androidId
							userData = new TokenVo();
							userData.setAndroidId(word);
							break;

						case 2: // corpNames
							userData.setCorpNames(word);
							break;

						case 4: // deviceToken
							userData.setDeviceToken(word);
							userDatas.add(userData);
							break;

						default:
							System.out.println("MyServerApplication Divide Error!");
					}
					word = "";
					j++;
					continue;
				}
			}
			word = word + ch;
		}
		return userDatas;
	}
}
