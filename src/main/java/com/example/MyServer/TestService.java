package com.example.MyServer;

import com.example.MyServer.domain.TestVo;
import com.example.MyServer.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    public TestMapper mapper;

    public List<TestVo> selectTest() {
        return mapper.selectTest();
    }
}
