package com.ispogsecbob.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.modules.enterprise.entity.EntTeacherAchievementInfoEntity;

import java.util.Map;

/**
 * 教师所获奖励信息表
 *
 * @author Mikey
 * @email 1625017540@qq.com
 * @date 2019-10-10 10:10:39
 */
public interface EntTeacherAchievementInfoService extends IService<EntTeacherAchievementInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(EntTeacherAchievementInfoEntity entTeacherAchievementInfoEntity);
}

