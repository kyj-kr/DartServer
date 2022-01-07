package com.example.MyServer.controller;


import com.example.MyServer.FirebaseCloudMessageService;
import com.example.MyServer.MyServerApplication;
import com.example.MyServer.domain.UserVo;
import com.example.MyServer.mapper.FirebaseDbService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class MsgController {

    @PostMapping(value = "/token")
    public void post(@RequestBody UserVo body) {
        String deviceToken = body.getDeviceToken();
        System.out.println(deviceToken);
        try {
            new FirebaseCloudMessageService().sendMessageTo(deviceToken, "Dart Title", "For Test", "1970.01.01", "");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/alarmList")
    public void putAlarmList(@RequestBody UserVo body) {
        String androidId = body.getAndroidId();
        String deviceToken = body.getDeviceToken();
        String corpInfos = body.getCorpNames();
        try {
            // db에 저장
            new FirebaseDbService().updateAlarmList(androidId, deviceToken, corpInfos);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}