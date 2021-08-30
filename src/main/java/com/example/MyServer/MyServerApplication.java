package com.example.MyServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.Param;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class MyServerApplication {

//	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	private static final String URL = "jdbc:mysql://localhost:3306/201724433?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
//	private static final String USER = "root";
//	private static final String PASSWORD = "1234";

	public static void main(String[] args) {
		SpringApplication.run(MyServerApplication.class, args);
//		try {
//			Class.forName(DRIVER);
//			Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//			System.out.println(connection);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
