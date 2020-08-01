package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;

import com.ispogsecbob.modules.enterprise.dao.EntCoopeationAttachDao;
import com.ispogsecbob.modules.enterprise.entity.EntCoopeationAttachEntity;
import com.ispogsecbob.modules.enterprise.service.EntCoopeationAttachService;


@Service("entCoopeationAttachService")
public class EntCoopeationAttachServiceImpl extends ServiceImpl<EntCoopeationAttachDao, EntCoopeationAttachEntity> implements EntCoopeationAttachService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntCoopeationAttachEntity> page = this.selectPage(
                new Query<EntCoopeationAttachEntity>(params).getPage(),
                new EntityWrapper<EntCoopeationAttachEntity>()
        );

        return new PageUtils(page);
    }

}