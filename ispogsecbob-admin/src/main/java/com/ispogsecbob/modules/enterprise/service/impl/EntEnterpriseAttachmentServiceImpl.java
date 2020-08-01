package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;
import com.ispogsecbob.modules.enterprise.dao.EntEnterpriseAttachmentDao;
import com.ispogsecbob.modules.enterprise.entity.EntEnterpriseAttachmentEntity;
import com.ispogsecbob.modules.enterprise.service.EntEnterpriseAttachmentService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("entEnterpriseAttachmentService")
public class EntEnterpriseAttachmentServiceImpl extends ServiceImpl<EntEnterpriseAttachmentDao, EntEnterpriseAttachmentEntity> implements EntEnterpriseAttachmentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntEnterpriseAttachmentEntity> page = this.selectPage(
                new Query<EntEnterpriseAttachmentEntity>(params).getPage(),
                new EntityWrapper<EntEnterpriseAttachmentEntity>()
        );

        return new PageUtils(page);
    }

}