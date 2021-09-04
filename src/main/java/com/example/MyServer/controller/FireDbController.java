package com.example.MyServer.controller;

import com.example.MyServer.FireDbService;
import com.example.MyServer.domain.TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireDbController {

    @Autowired
    FireDbService fireDbService;

    @PostMapping(value = "/alarmInsert")
    public String createUser(@RequestBody TokenVo tokenVo) throws Exception {
        return fireDbService.createUserDetail(tokenVo);
    }

    @PostMapping(value = "/alarmUpdate")
    public String updateUser(@RequestBody TokenVo tokenVo) throws Exception {
        return fireDbService.updateUserDetail(tokenVo);
    }

    @PostMapping(value = "/userRemove")
    public String deleteUser(@RequestBody TokenVo tokenVo) throws Exception {
        return fireDbService.deleteUserDetail(tokenVo.getDeviceToken());
    }
}
