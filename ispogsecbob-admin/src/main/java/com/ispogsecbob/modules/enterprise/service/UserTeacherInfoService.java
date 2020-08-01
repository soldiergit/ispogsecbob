package com.ispogsecbob.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.modules.enterprise.entity.EntUserTeacherInfoEntity;
import com.ispogsecbob.modules.sys.entity.SysUserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @date 2018/11/8 17:18
 * @Version 1.0
 */
public interface UserTeacherInfoService extends IService<EntUserTeacherInfoEntity> {

    List<SysUserEntity> queryTeacher(Map<String, Object> params);
    List<EntUserTeacherInfoEntity> queryProjectTeacherInfo(Long projectId);
    List<EntUserTeacherInfoEntity> queryMatchTeacherInfo(Long matchId);
    List<EntUserTeacherInfoEntity> queryDeclareTeacherInfo(Long declareId);
    List<EntUserTeacherInfoEntity> queryFinishTeacherInfo(Long declareId);
    List<EntUserTeacherInfoEntity> queryAllTeacherInfo();
    EntUserTeacherInfoEntity queryByUserId(Long userId);

    @Transactional
    void saveOrUpdate(EntUserTeacherInfoEntity EntUserTeacherInfoEntity);

    /**
     * 用户id 查教师id
     * @param userId
     * @return
     */
    Long queryUserTeacherIdByUserId(Long userId);

}
