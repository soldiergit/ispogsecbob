package com.ispogsecbob.modules.enterprise.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ispogsecbob.modules.enterprise.entity.EntInstituteEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface EntInstituteDao extends BaseMapper<EntInstituteEntity> {

    EntInstituteEntity queryByInstituteId(Map<String, Object> params);

    List<EntInstituteEntity> queryAllInstitute();
}
