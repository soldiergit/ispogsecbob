package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ispogsecbob.modules.enterprise.dao.EntUserPersonInfoDao;
import com.ispogsecbob.modules.enterprise.entity.EntUserPersonInfoEntity;
import com.ispogsecbob.modules.enterprise.service.UserPerInfoService;
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
 * @date 2018/11/8 17:40
 * @Version 1.0
 */
@Service
public class UserPerInfoServiceImpl extends ServiceImpl<EntUserPersonInfoDao, EntUserPersonInfoEntity> implements UserPerInfoService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserPerInfoService userPerInfoService;

    @Override
    public List<EntUserPersonInfoEntity> queryAllPersonInfo(Map<String, Object> params) {
        return baseMapper.queryAllPersonInfo(params);
    }

    @Override
    public Long deleteByProjectId(Long projectId) {
        return baseMapper.deleteByProjectId(projectId);
    }

    @Override
    public EntUserPersonInfoEntity queryByUserId(Long userId) {
        EntUserPersonInfoEntity entUserPersonInfoEntity = baseMapper.queryByUserId(userId);
        SysUserEntity sysUserEntity = sysUserService.selectById(userId);
        if (null == entUserPersonInfoEntity) {
            entUserPersonInfoEntity = new EntUserPersonInfoEntity();
        }
        entUserPersonInfoEntity.setSysUserEntity(sysUserEntity);

        return entUserPersonInfoEntity;
    }

    @Override
    public void saveOrUpdate(EntUserPersonInfoEntity entUserPersonInfoEntity) {
        System.out.println("这是" + new Gson().toJson(entUserPersonInfoEntity));;
        SysUserEntity sysUserEntity = entUserPersonInfoEntity.getSysUserEntity();
        entUserPersonInfoEntity.setSysUserEntity(null);
        this.insertOrUpdate(entUserPersonInfoEntity);
        sysUserService.insertOrUpdate(sysUserEntity);
    }

    /**
     * 根据学院id查找该学院下的全部负责人
     * @param instituteId
     * @return
     */
    @Override
    public List<EntUserPersonInfoEntity> queryByUserInstituteIds(Long instituteId) {
        return baseMapper.queryByUserInstituteIds(instituteId);
    }

    @Override
    public Long queryUserPerIdByUserId(Long userId) {
        return baseMapper.queryUserPerIdByUserId(userId);
    }
}
