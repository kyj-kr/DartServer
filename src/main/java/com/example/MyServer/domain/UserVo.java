package com.example.MyServer.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UserVo {
    private String androidId;
    private String deviceToken;
    private String corpNames;
    private ArrayList<NotiVo> notiVos = new ArrayList<>();


    // notiVos의 notifiedReceptNum에 일치하는 문자열이 있는지 확인
    // notiVos를 위한 메소드
    public boolean isContains(String receptNum) {
        for(NotiVo notiVo : notiVos) {
            String notifedReceptNum = notiVo.getNotifedReceptNum();
            if(receptNum.equals(notifedReceptNum)) {
                return true;
            }
        }
        return false;
    }
}
