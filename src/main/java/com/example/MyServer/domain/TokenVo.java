package com.example.MyServer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenVo {
    private String androidId;
    private String deviceToken;
    private String corpNames;
}
