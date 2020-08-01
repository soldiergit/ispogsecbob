package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.modules.enterprise.dao.EntStudentAchievementInfoDao;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;

import com.ispogsecbob.modules.enterprise.entity.EntStudentAchievementInfoEntity;
import com.ispogsecbob.modules.enterprise.service.EntStudentAchievementInfoService;


@Service("entStudentAchievementInfoService")
public class EntStudentAchievementInfoServiceImpl extends ServiceImpl<EntStudentAchievementInfoDao, EntStudentAchievementInfoEntity> implements EntStudentAchievementInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Object userId = params.get("userId");
        Object hasApply = params.get("hasApply");

        EntityWrapper<EntStudentAchievementInfoEntity> ew = new EntityWrapper<>();
        //  项目负责人查询
        if (userId!=null&&userId!="") ew.eq("user_per_id",Long.parseLong(userId.toString()));
        //1:未审批 2:已通过 3:未通过
        if (hasApply!=null)ew.eq("in_apply",Long.parseLong(hasApply.toString()));

        Page<EntStudentAchievementInfoEntity> page = this.selectPage(
                new Query<EntStudentAchievementInfoEntity>(params).getPage(), ew
        );
        return new PageUtils(page);
    }

}