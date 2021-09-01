package com.example.MyServer;

import com.example.MyServer.domain.TokenVo;
import com.example.MyServer.mapper.DbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {
    @Autowired
    public DbMapper mapper;

    public void insertAlarm(TokenVo tokenVo) { mapper.insertAlarm(tokenVo); }

    public void updateAlarm(TokenVo tokenVo) { mapper.updateAlarm(tokenVo); }

    public void deleteUser(TokenVo tokenVo) { mapper.deleteUser(tokenVo); }
}
