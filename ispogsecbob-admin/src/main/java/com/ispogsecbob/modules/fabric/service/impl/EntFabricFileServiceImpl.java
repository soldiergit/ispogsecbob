package com.ispogsecbob.modules.fabric.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;

import com.ispogsecbob.modules.fabric.dao.EntFabricFileDao;
import com.ispogsecbob.modules.fabric.entity.EntFabricFileEntity;
import com.ispogsecbob.modules.fabric.service.EntFabricFileService;


@Service("entFabricFileService")
public class EntFabricFileServiceImpl extends ServiceImpl<EntFabricFileDao, EntFabricFileEntity> implements EntFabricFileService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        EntityWrapper<EntFabricFileEntity> ew = new EntityWrapper<>();

        if (params.get("key")!=null)ew.like("file_name",params.get("key").toString());

        Page<EntFabricFileEntity> page = this.selectPage(
                new Query<EntFabricFileEntity>(params).getPage(),
                ew
        );

        List<EntFabricFileEntity> records = page.getRecords();

        records.forEach(v->{
            v.setSysUserEntity(sysUserService.selectById(v.getUserId()));
        });

        return new PageUtils(page);
    }

}