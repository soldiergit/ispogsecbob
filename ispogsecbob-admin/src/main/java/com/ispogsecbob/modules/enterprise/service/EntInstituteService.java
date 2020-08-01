package com.ispogsecbob.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.modules.enterprise.entity.EntInstituteEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: Mikey
 * Email：biaogejiushibiao@outlook.com
 * Date: 2018/11/6
 **/
public interface EntInstituteService extends IService<EntInstituteEntity> {
    PageUtils queryPage(Map<String, Object> params);
    List<EntInstituteEntity> queryAllInstitute();
    @Transactional
    void total(Map<String, Object> params);

    EntInstituteEntity queryByInstituteId(Map<String, Object> params);
}
