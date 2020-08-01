package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;

import com.ispogsecbob.modules.enterprise.dao.EntProjectAttachDao;
import com.ispogsecbob.modules.enterprise.entity.EntProjectAttachEntity;
import com.ispogsecbob.modules.enterprise.service.EntProjectAttachService;


@Service("entProjectAttachService")
public class EntProjectAttachServiceImpl extends ServiceImpl<EntProjectAttachDao, EntProjectAttachEntity> implements EntProjectAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntProjectAttachEntity> page = this.selectPage(
                new Query<EntProjectAttachEntity>(params).getPage(),
                new EntityWrapper<EntProjectAttachEntity>()
        );

        return new PageUtils(page);
    }

}