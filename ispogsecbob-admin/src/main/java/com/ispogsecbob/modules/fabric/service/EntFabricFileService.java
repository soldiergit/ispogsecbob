package com.ispogsecbob.modules.fabric.service;

//import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.modules.fabric.entity.EntFabricFileEntity;

import java.util.Map;

/**
 * 
 *
 * @author Mikey
 * @email biaogejiushibiao@outlook.com
 * @date 2020-08-06 09:52:59
 */
public interface EntFabricFileService extends IService<EntFabricFileEntity> {

    PageUtils queryPage(Map<String, Object> params);

    EntFabricFileEntity findBySHA256(String sha_256);
}

