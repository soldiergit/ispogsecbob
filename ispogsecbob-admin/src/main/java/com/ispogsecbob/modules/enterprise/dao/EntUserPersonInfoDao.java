package com.ispogsecbob.modules.enterprise.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ispogsecbob.modules.enterprise.entity.EntUserPersonInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @date 2018/11/8 16:21
 * @Version 1.0
 */
@Mapper
public interface EntUserPersonInfoDao extends BaseMapper<EntUserPersonInfoEntity> {
    List<EntUserPersonInfoEntity> queryAllPersonInfo(Map<String, Object> params);
    Long deleteByProjectId(Long projectId);
    EntUserPersonInfoEntity queryByUserId(Long userId);

    List<EntUserPersonInfoEntity> queryByUserInstituteIds(Long instituteId);

    /**
     * 用户id 查学生id
     * @param userId
     * @return
     */
    Long queryUserPerIdByUserId(Long userId);

}
