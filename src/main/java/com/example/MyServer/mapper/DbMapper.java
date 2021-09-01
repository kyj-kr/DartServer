package com.example.MyServer.mapper;

import com.example.MyServer.domain.TokenVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DbMapper {
    void insertAlarm(TokenVo tokenVo);
    void updateAlarm(TokenVo tokenVo);
    void deleteUser(TokenVo tokenVo);
}
