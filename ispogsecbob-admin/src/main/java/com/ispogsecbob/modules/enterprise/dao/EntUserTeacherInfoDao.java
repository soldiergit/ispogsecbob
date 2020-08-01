package com.ispogsecbob.modules.enterprise.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ispogsecbob.modules.enterprise.entity.EntUserTeacherInfoEntity;
import com.ispogsecbob.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @date 2018/11/8 16:07
 * @Version 1.0
 */
@Mapper
public interface EntUserTeacherInfoDao extends BaseMapper<EntUserTeacherInfoEntity> {

    List<SysUserEntity> queryTeacher(Map<String, Object> params);

    List<EntUserTeacherInfoEntity> queryProjectTeacherInfo(Long projectId);
    List<EntUserTeacherInfoEntity> queryMatchTeacherInfo(Long matchId);
    List<EntUserTeacherInfoEntity> queryDeclareTeacherInfo(Long declareId);
    List<EntUserTeacherInfoEntity> queryFinishTeacherInfo(Long finishId);
    List<EntUserTeacherInfoEntity> queryAllTeacherInfo();
    List<EntUserTeacherInfoEntity> queryAll(Long userTeacherId);
    Long deleteByProjectId(Long projectId);
    EntUserTeacherInfoEntity queryByUserId(Long userId);

    /**
     * 用户id 查教师id
     * @param userId
     * @return
     */
    Long queryUserTeacherIdByUserId(Long userId);

}
