package com.neusoft.mapper;

import com.neusoft.domain.User;

import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByEmail(String email);

    User selectByEmailAndPass (User record);

    int updatepasswdByPrimaryKeySelective (User record);//更新密码

    int updatebasicByPrimaryKeySelective (User record);//更新基本资料

    int updatepicByPrimaryKeySelective (User record);//上传头像
}