package com.ispogsecbob.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ispogsecbob.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * @author: Mikey
 * Email：biaogejiushibiao@outlook.com
 * Date: 2018/12/4
 **/
@Data
@TableName("ent_user_teacher_info")
public class EntUserTeacherInfoEntity {
    @TableId
    private Long userTeacherId;
    private Long userId;
    private String teacherCardNo;
    private Long teacherSex;
    private String teacherPost;
    private Long teacherTitle;
    @TableField(exist = false)
    private EntPersonCooperationInfoEntity entPersonCooperationInfoEntity;
    @TableField(exist = false)
    private SysUserEntity sysUserEntity;
    @TableField(exist = false)
    private Long isDel;
    private Integer teacherBackground;
    private Integer teacherDegree;
    private String teacherStudy;
    private String teacherScientific;
}
