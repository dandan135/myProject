package com.cssl.service;

import com.cssl.dao.UsersMapper;
import com.cssl.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UsersService {

    @Resource
    private UsersMapper usersMapper;

    //登录
    public Users log(Users users){
        return usersMapper.select(users);
    }
    //注册
    public int reg(Users users){
        return usersMapper.insert(users);
    }
    //改变状态时间
    public int change(Users users){return usersMapper.update(users);}
    //验证用户名唯一
    public int isReg(String userName){return usersMapper.isReg(userName);}
}
