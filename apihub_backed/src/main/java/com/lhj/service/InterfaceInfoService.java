package com.lhj.service;

import com.lhj.apicommon.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Li
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-14 17:06:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
