package com.example.MyServer.controller;

import com.example.MyServer.DbService;
import com.example.MyServer.domain.CorpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class DbController {
    @Autowired
    DbService dbService;

    @PostMapping(value = "/201724433")
    public void test() throws Exception {
        List<CorpVo> corpVoList = dbService.selectTest();
        for(CorpVo corpVo : corpVoList) {
            System.out.println(corpVo.getName());
        }
    }
}
