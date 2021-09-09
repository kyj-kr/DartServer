package com.example.MyServer.mapper;

import com.example.MyServer.domain.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DbMapper {
    void insertAlarm(UserVo userVo);
    void updateAlarm(UserVo userVo);
    void deleteUser(UserVo userVo);
}
