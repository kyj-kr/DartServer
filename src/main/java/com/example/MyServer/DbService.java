package com.example.MyServer;

import com.example.MyServer.domain.CorpVo;
import com.example.MyServer.mapper.DbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {
    @Autowired
    public DbMapper mapper;

    public List<CorpVo> selectTest() {
        return mapper.selectTest();
    }
}
