package com.lhj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhj.apicommon.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author Li
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-09-14 17:07:24
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(@Param("limit") int limit);

}




