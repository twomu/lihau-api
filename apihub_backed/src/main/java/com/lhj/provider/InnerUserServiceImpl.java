package com.lhj.provider;


import com.lhj.apicommon.entity.User;
import com.lhj.apicommon.service.InnerUserService;

import com.lhj.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public User getUserById(Long userId) {
        return userService.getById(userId);
    }
}
