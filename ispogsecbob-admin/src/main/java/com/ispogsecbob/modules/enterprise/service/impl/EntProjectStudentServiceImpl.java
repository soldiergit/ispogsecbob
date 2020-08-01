package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;
import com.ispogsecbob.modules.enterprise.dao.EntProjectStudentDao;
import com.ispogsecbob.modules.enterprise.entity.EntProjectStudentEntity;
import com.ispogsecbob.modules.enterprise.service.EntProjectStudentService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("entProjectStudentService")
public class EntProjectStudentServiceImpl extends ServiceImpl<EntProjectStudentDao, EntProjectStudentEntity> implements EntProjectStudentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntProjectStudentEntity> page = this.selectPage(
                new Query<EntProjectStudentEntity>(params).getPage(),
                new EntityWrapper<EntProjectStudentEntity>()
        );

        return new PageUtils(page);
    }

}