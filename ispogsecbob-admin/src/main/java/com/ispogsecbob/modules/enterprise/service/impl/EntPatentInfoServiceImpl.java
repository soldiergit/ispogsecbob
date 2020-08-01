package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;
import com.ispogsecbob.modules.enterprise.dao.EntPatentInfoDao;
import com.ispogsecbob.modules.enterprise.entity.EntPatentInfoEntity;
import com.ispogsecbob.modules.enterprise.service.EntPatentInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("entPatentInfoService")
public class EntPatentInfoServiceImpl extends ServiceImpl<EntPatentInfoDao, EntPatentInfoEntity> implements EntPatentInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntPatentInfoEntity> page = this.selectPage(
                new Query<EntPatentInfoEntity>(params).getPage(),
                new EntityWrapper<EntPatentInfoEntity>()
        );

        return new PageUtils(page);
    }

}