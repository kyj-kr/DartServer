package com.example.MyServer.mapper;

import com.example.MyServer.domain.CorpVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DbMapper {
    List<CorpVo> selectTest();
}
