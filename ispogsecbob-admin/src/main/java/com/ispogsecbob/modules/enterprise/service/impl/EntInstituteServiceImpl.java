package com.ispogsecbob.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ispogsecbob.common.utils.PageUtils;
import com.ispogsecbob.common.utils.Query;
import com.ispogsecbob.modules.enterprise.dao.EntInstituteDao;
import com.ispogsecbob.modules.enterprise.entity.EntInstituteEntity;
import com.ispogsecbob.modules.enterprise.service.EntInstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EntInstituteServiceImpl extends ServiceImpl<EntInstituteDao, EntInstituteEntity> implements EntInstituteService {

    @Autowired
    private EntInstituteService instituteService;
    @Autowired


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EntInstituteEntity> page = this.selectPage(
                new Query<EntInstituteEntity>(params).getPage(),
                new EntityWrapper<EntInstituteEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<EntInstituteEntity> queryAllInstitute() {
        return baseMapper.queryAllInstitute();
    }

    @Override
    public void total(Map<String, Object> params) {

        EntInstituteEntity instituteEntity = instituteService.queryByInstituteId(params);

        //统计申报项目总数
//        Long DeclareProject = declareInfoService.queryDeclareProjectNum(params);
        //统计申报创新训练项目数
//        Long NewProject = declareInfoService.queryNewProjectNum(params);
        //统计申报创业训练项目数
//        Long TrainProject = declareInfoService.queryTrainProjectNum(params);
        //统计申报创业实践项目数
//        Long PracticeProject = declareInfoService.queryPracticeProjectNum(params);
        //统计参与申报项目学生数
//        Long studentNum = declareStaffInfoService.queryStaffNum(params);
        //统计参与申报项目导师数
//        Long teacherNum = declareTeacherService.queryTeacherNum(params);

//        instituteEntity.setDeclareProjectNum(DeclareProject);
//        instituteEntity.setDeclareNewProjectNum(NewProject);
//        instituteEntity.setDeclareTrainProjectNum(TrainProject);
//        instituteEntity.setDeclarePracticeProjectNum(PracticeProject);
//        instituteEntity.setDeclareStudentNum(studentNum);
//        instituteEntity.setDeclareTeacherNum(teacherNum);

        instituteService.insertOrUpdate(instituteEntity);
    }

    @Override
    public EntInstituteEntity queryByInstituteId(Map<String, Object> params) {
        return baseMapper.queryByInstituteId(params);
    }
}
