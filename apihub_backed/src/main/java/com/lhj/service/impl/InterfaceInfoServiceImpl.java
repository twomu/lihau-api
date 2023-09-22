package com.lhj.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhj.apicommon.common.ErrorCode;
import com.lhj.apicommon.entity.InterfaceInfo;
import com.lhj.apicommon.exception.BusinessException;
import com.lhj.apicommon.exception.ThrowUtils;
import com.lhj.mapper.InterfaceInfoMapper;
import com.lhj.service.InterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author Li
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-09-14 17:06:19
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {


        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        String name = interfaceInfo.getName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称不能为空");
        }
        if (name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
    }
}




