package com.ispogsecbob.modules.enterprise.service;

import com.baomidou.mybatisplus.service.IService;
import com.ispogsecbob.modules.enterprise.entity.EntUserPersonInfoEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Mikey
 * @Title:
 * @Description:
 * @date 2018/11/8 17:13
 * @Version 1.0
 */
public interface UserPerInfoService extends IService<EntUserPersonInfoEntity> {
    List<EntUserPersonInfoEntity> queryAllPersonInfo(Map<String, Object> params);

    EntUserPersonInfoEntity queryByUserId(Long userId);
    Long deleteByProjectId(Long projectId);
    @Transactional
    void saveOrUpdate(EntUserPersonInfoEntity entUserPersonInfoEntity);


    List<EntUserPersonInfoEntity> queryByUserInstituteIds(Long instituteId);

    /**
     * 用户id 查学生id
     * @param userId
     * @return
     */
    Long queryUserPerIdByUserId(Long userId);

}
