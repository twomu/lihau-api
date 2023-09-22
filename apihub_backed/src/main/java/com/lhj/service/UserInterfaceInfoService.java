package com.lhj.service;

import com.lhj.apicommon.entity.UserInterfaceInfo;

import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Li
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-14 17:07:24
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    boolean invokeCount(long userId, long interfaceInfoId);

    int getLeftInvokeCount(long userId, long interfaceInfoId);
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);
}
