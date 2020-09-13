package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 孔佳齐丶
 * @create 2020-09-12 10:59
 * @package com.atguigu.eduservice.controller.front
 */
@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService eduCourseService;

    //查询前8条热门课程数据,查询前四条名师数据
    @GetMapping("index")
    public R findCourseAndTeacher(){
        //1.新建热门课程查询条件
        //QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        //2.新增查询条件
        //courseQueryWrapper.orderByAsc("id");
        //3.增加后缀
        //courseQueryWrapper.last(" limit 8 ");
        //4.查询获取集合
        //TODO 使用@Cacheable 将主页数据存到redis中
        //List<EduCourse> courseList = eduCourseService.list(courseQueryWrapper);
        List<EduCourse> courseList = eduCourseService.listAndAddCacheToRedis();

        //5.新建名师查询条条件
        //QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        //同上
        //teacherQueryWrapper.orderByAsc("id");
        //teacherQueryWrapper.last(" limit 4 ");
        //List<EduTeacher> teacherList  = eduTeacherService.list(teacherQueryWrapper);
        List<EduTeacher> teacherList  = eduTeacherService.listAndAddTeacherToRedis();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }

}
