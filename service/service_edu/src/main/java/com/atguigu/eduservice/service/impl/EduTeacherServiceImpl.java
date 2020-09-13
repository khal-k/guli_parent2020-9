package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-02-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    @Cacheable(key="'listFamousTeacher'",value = "famousTeacher")
    public List<EduTeacher> listAndAddTeacherToRedis() {
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        //同上
        teacherQueryWrapper.orderByAsc("id");
        teacherQueryWrapper.last(" limit 4 ");
        List<EduTeacher> teacherList = baseMapper.selectList(teacherQueryWrapper);
        return teacherList;
    }
}
