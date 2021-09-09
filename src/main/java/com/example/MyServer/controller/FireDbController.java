package com.example.MyServer.controller;

import com.example.MyServer.mapper.FireDbServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireDbController {

//    @Autowired
//    FireDbService fireDbService;
    @Autowired
    FireDbServiceTest fireDbServiceTest;

//    @PostMapping(value = "/alarmInsert")
//    public String createUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbService.createUserDetail(tokenVo);
//    }
//
//    @PostMapping(value = "/alarmUpdate")
//    public String updateUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbService.updateUserDetail(tokenVo);
//    }
//
//    @PostMapping(value = "/userRemove")
//    public String deleteUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbService.deleteUserDetail(tokenVo.getDeviceToken());
//    }

//    @PostMapping(value = "/alarmInsert")
//    public String createUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbServiceTest.createUserDetail(tokenVo);
//    }
//
//    @PostMapping(value = "/alarmUpdate")
//    public String updateUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbServiceTest.updateUserDetail(tokenVo);
//    }
//
//    @PostMapping(value = "/userRemove")
//    public String deleteUser(@RequestBody TokenVo tokenVo) throws Exception {
//        return fireDbServiceTest.deleteUserDetail(tokenVo.getDeviceToken());
//    }
}
