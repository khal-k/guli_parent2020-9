package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.LoginVo;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-13
 */
@RestController
@RequestMapping("/educenter/ucenter")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //1.用户登录功能
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        //1.登录,如果成功,设置jwt的token
        String token = ucenterMemberService.login(loginVo);
        return R.ok().data("token",token);
    }

    //2.用户注册功能
    @PostMapping("register")
    public R registerMember(@RequestBody RegisterVo registerVo){
        //注册
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    //3.登录成功后,根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //1.获取token中用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //2.根据id获取用户对象
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("memberInfo",member);
    }

}

