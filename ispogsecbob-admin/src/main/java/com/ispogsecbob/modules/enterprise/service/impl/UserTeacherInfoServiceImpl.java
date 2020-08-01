package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.modules.enterprise.dao.EntUserTeacherInfoDao;
import com.ispogsecbob.modules.enterprise.entity.EntUserTeacherInfoEntity;
import com.ispogsecbob.modules.enterprise.service.UserTeacherInfoService;
import com.ispogsecbob.modules.sys.entity.SysUserEntity;
import com.ispogsecbob.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @date 2018/11/8 17:51
 * @Version 1.0
 */
@Service
public class UserTeacherInfoServiceImpl extends ServiceImpl<EntUserTeacherInfoDao, EntUserTeacherInfoEntity> implements UserTeacherInfoService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysUserEntity> queryTeacher(Map<String, Object> params) {
        return baseMapper.queryTeacher(params);
    }

    @Override
    public List<EntUserTeacherInfoEntity> queryProjectTeacherInfo(Long projectId) {
        return baseMapper.queryProjectTeacherInfo(projectId);
    }

    @Override
    public List<EntUserTeacherInfoEntity> queryMatchTeacherInfo(Long matchId) {
        return baseMapper.queryMatchTeacherInfo(matchId);
    }

    @Override
    public List<EntUserTeacherInfoEntity> queryDeclareTeacherInfo(Long declareId) {
        return baseMapper.queryDeclareTeacherInfo(declareId);
    }

    @Override
    public List<EntUserTeacherInfoEntity> queryFinishTeacherInfo(Long finishId) {
        return baseMapper.queryFinishTeacherInfo(finishId);
    }

    @Override
    public List<EntUserTeacherInfoEntity> queryAllTeacherInfo() {
        return baseMapper.queryAllTeacherInfo();
    }

    @Override
    public EntUserTeacherInfoEntity queryByUserId(Long userId) {
        EntUserTeacherInfoEntity EntUserTeacherInfoEntity = baseMapper.queryByUserId(userId);
        SysUserEntity sysUserEntity = sysUserService.selectById(userId);
        if (null == EntUserTeacherInfoEntity) {
            EntUserTeacherInfoEntity = new EntUserTeacherInfoEntity();
        }
        EntUserTeacherInfoEntity.setSysUserEntity(sysUserEntity);

        return EntUserTeacherInfoEntity;
    }

    @Override
    public void saveOrUpdate(EntUserTeacherInfoEntity EntUserTeacherInfoEntity) {
        SysUserEntity sysUserEntity = EntUserTeacherInfoEntity.getSysUserEntity();
        EntUserTeacherInfoEntity.setSysUserEntity(null);
        this.insertOrUpdate(EntUserTeacherInfoEntity);
        sysUserService.insertOrUpdate(sysUserEntity);
    }

    @Override
    public Long queryUserTeacherIdByUserId(Long userId) {
        return baseMapper.queryUserTeacherIdByUserId(userId);
    }
}
