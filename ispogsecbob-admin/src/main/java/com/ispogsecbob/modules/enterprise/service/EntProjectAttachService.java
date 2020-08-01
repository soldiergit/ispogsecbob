package com.ispogsecbob.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.modules.enterprise.entity.EntProjectAttachEntity;

import java.util.Map;

/**
 * 项目附件表
 *
 * @author 莫智帆
 * @email 1625017540@qq.com
 * @date 2019-11-12 15:45:44
 */
public interface EntProjectAttachService extends IService<EntProjectAttachEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

