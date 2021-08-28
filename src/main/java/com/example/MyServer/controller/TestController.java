package com.example.MyServer.controller;

import com.example.MyServer.TestService;
import com.example.MyServer.domain.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @RequestMapping(value = "/201724433")
    public ModelAndView test() throws Exception {
        ModelAndView mav = new ModelAndView("test");

        List<TestVo> testVoList = testService.selectTest();
        System.out.println(testVoList);
        mav.addObject("list", testVoList);

        return mav;
    }
}
