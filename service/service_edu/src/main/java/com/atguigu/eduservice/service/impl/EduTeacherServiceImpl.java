package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //页面教室分页
    @Override
    public Map<String ,Object> getFrontTeacher(Page<EduTeacher> eduTeacherIPage) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        baseMapper.selectPage(eduTeacherIPage, wrapper);

        //获取分页得到的数据
        List<EduTeacher> teacherList = eduTeacherIPage.getRecords();
        long current = eduTeacherIPage.getCurrent();
        long pageNum = eduTeacherIPage.getPages();
        long pageSize =eduTeacherIPage.getSize();
        long pageTotal =eduTeacherIPage.getTotal();
        boolean hasNext = eduTeacherIPage.hasNext();
        boolean hasPrevious = eduTeacherIPage.hasPrevious();

        //存储到map集合
        Map<String ,Object> map = new HashMap<>();
        map.put("teacherList",teacherList);
        map.put("current",current);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        map.put("pageTotal",pageTotal);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }
}
