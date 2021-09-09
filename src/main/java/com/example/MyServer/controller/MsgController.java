package com.example.MyServer.controller;


import com.example.MyServer.FirebaseCloudMessageService;
import com.example.MyServer.domain.UserVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MsgController {

    @PostMapping(value = "/token")
    public void post(@RequestBody UserVo body) {
        String deviceToken = body.getDeviceToken();
        System.out.println(deviceToken);
        try {
            new FirebaseCloudMessageService().sendMessageTo(deviceToken, "Dart Title", "For Test");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}