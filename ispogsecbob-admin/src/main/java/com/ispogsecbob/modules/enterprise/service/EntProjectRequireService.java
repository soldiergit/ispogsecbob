package com.ispogsecbob.modules.enterprise.service;


import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.modules.enterprise.entity.EntProjectRequireEntity;

import java.util.Map;

/**
 * 项目需求表
 *
 * @author mozhifan
 * @email 1217567927@qq.com
 * @date 2019-09-10 22:19:50
 */
public interface EntProjectRequireService extends IService<EntProjectRequireEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

