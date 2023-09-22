package com.lhj.apicommon.service;


import com.lhj.apicommon.entity.InterfaceInfo;
import com.lhj.apicommon.entity.User;

/**
 * 接口管理系统的公共远程调用接口
 */
public interface ApiBackendService {


    /**
     * 根据accessKey和secretKey获取接口调用用户的信息，判断其是否有权限
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);


    /**
     * 根据接口的url和接口的请求方式获取调用接口详情
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterFaceInfo(String url, String method);



    /**
     * 统计接口调用次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long userId,long interfaceInfoId);


    /**
     * 根获取用户所拥有的接口剩余调用次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    int getLeftInvokeCount(long userId,long interfaceInfoId);


    /**
     * 根据接口id获取接口详情
     */
    InterfaceInfo getInterfaceById(long interfaceId);

    boolean recoverInterfaceLeft(long interfaceInfoId,long userId);


}
