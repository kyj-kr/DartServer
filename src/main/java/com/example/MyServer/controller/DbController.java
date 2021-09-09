package com.example.MyServer.controller;

import com.example.MyServer.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DbController {
    @Autowired
    DbService dbService;

//    @PostMapping(value = "/alarmInsert")
//    public void insertTokenAndCorpNames(@RequestBody TokenVo tokenVo) throws Exception {
//        String deviceToken = tokenVo.getDeviceToken();
//        String corpNames = tokenVo.getCorpNames();
//        System.out.println("deviceToken: " + deviceToken);
//        System.out.println("corpNames: " + corpNames);
//
//        dbService.insertAlarm(tokenVo);
//    }
//
//    @PostMapping(value = "/alarmUpdate")
//    public void updateTokenAndCorpNames(@RequestBody TokenVo tokenVo) throws Exception {
//        dbService.updateAlarm(tokenVo);
//    }
//
//    @PostMapping(value = "/userRemove")
//    public void userRemove(@RequestBody TokenVo tokenVo) throws Exception {
//        dbService.deleteUser(tokenVo);
//    }
}
