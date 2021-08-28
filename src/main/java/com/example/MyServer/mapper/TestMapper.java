package com.example.MyServer.mapper;

import com.example.MyServer.domain.TestVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {
    List<TestVo> selectTest();
}
