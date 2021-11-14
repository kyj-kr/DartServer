package com.example.MyServer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class NotiVo {

    private String corpName;
    private String time;
    private String notifedReceptNum;
    private boolean isMessaged;


    public static String getCorpList(ArrayList<NotiVo> list) {
        String corpList = "";
        for(NotiVo notiVo : list) {
            if(!notiVo.isMessaged()) {
                notiVo.setMessaged(true);
                if(!corpList.contains(notiVo.getCorpName())) {
                    if(corpList.equals("")) {
                        corpList = corpList + notiVo.getCorpName() + "/" + notiVo.getTime();
                    }
                    else {
                        corpList = corpList + "," + notiVo.getCorpName() + "/" + notiVo.getTime();
                    }
                }
            }
        }
        return corpList;
    }
}
