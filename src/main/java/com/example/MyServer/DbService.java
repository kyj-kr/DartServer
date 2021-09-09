package com.example.MyServer;

import com.example.MyServer.domain.UserVo;
import com.example.MyServer.mapper.DbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {
    @Autowired
    public DbMapper mapper;

    public void insertAlarm(UserVo userVo) { mapper.insertAlarm(userVo); }

    public void updateAlarm(UserVo userVo) { mapper.updateAlarm(userVo); }

    public void deleteUser(UserVo userVo) { mapper.deleteUser(userVo); }
}
