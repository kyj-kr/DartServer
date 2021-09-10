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

}
