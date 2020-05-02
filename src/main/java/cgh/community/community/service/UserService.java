package cgh.community.community.service;

import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Akuma
 * @date 2020/5/2 14:50
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户创建或更新用户
     * @param user
     */
    public void createOrUpdate(User user) {
        //根据accountId查询用户是否存在
        User ddUser = userMapper.findByAccountId(user.getAccountId());
        if(ddUser == null){ //用户为空，则创建
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else{   //用户不为空，则更新
            ddUser.setGmtModified(System.currentTimeMillis());
            ddUser.setAvatarUrl(user.getAvatarUrl());
            ddUser.setName(user.getName());
            ddUser.setToken(user.getToken());
            userMapper.update(ddUser);
        }
    }
}
