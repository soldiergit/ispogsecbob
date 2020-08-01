package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;
import com.ispogsecbob.modules.enterprise.dao.EntProjectRequireDao;
import com.ispogsecbob.modules.enterprise.entity.EntProjectRequireEntity;
import com.ispogsecbob.modules.enterprise.service.EntProjectRequireService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("entProjectRequireService")
public class EntProjectRequireServiceImpl extends ServiceImpl<EntProjectRequireDao, EntProjectRequireEntity> implements EntProjectRequireService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntProjectRequireEntity> page = this.selectPage(
                new Query<EntProjectRequireEntity>(params).getPage(),
                new EntityWrapper<EntProjectRequireEntity>()
        );

        return new PageUtils(page);
    }

}