package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 孔佳齐丶
 * @create 2020-09-15 14:07
 * @package com.atguigu.eduservice.controller.front
 */
@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    //1.教师显示分页
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable long current,@PathVariable long limit){
        //1.分页
        Page<EduTeacher> page = new Page(current,limit);
        //2.执行分页查询
        Map<String ,Object> map = eduTeacherService.getFrontTeacher(page);
        return R.ok().data(map);
    }

    //2.讲师详细信息和课程列表
    @GetMapping("getTeacherInfoAndCourse/{teacherId}")
    public R getTeacherInfoAndCourse(@PathVariable("teacherId") String teacherId){
        //1.查询讲师信息
        EduTeacher teacherInfo = eduTeacherService.getById(teacherId);

        //2.查询讲师所教课程列表
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        List<EduCourse> list = eduCourseService.list(wrapper);
        return R.ok().data("teacherInfo",teacherInfo).data("courseList",list);
    }
}
