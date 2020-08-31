package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.dto.AddressDTO;
import com.example.mybatisdemo.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDAO{

    @Select("select * from user")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "address",column = "address")
    })
    List<User> getAll();

    @Delete("delete from user where id = #{id}")
    int delete(Integer id);

    List<AddressDTO> countByAddress();

    int add(User record);

    int batchInsert(List<User> list);

    int batchUpdate(List<User> list);

}
