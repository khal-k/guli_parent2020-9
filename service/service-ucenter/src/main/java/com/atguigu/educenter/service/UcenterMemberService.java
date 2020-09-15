package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.LoginVo;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-13
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录
    String login(LoginVo loginVo);

    //2.用户注册功能
    void register(RegisterVo registerVo);

    //7.根据微信用户唯一的openid判断数据库中是否存在该用户
    UcenterMember selectUMemberByOpenId(String userOpenId);
}
