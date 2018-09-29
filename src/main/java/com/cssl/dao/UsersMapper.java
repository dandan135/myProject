package com.cssl.dao;

import com.cssl.pojo.Users;

public interface UsersMapper {
    //登录
    public Users select(Users users);
    //注册
    public int insert(Users users);
    //登录改变状态和登录时间
    public int update(Users users);
    //验证用户名是否唯一
    public int isReg(String userName);
}
