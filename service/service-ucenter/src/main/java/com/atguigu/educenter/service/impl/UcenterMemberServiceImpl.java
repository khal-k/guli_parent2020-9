package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.LoginVo;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-13
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //1.登录功能
    @Override
    public String login(LoginVo loginVo) {
        //1.获取登录用户的密码和手机号
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //2.判断密码和手机号是否为空
        if(mobile==null||password==null){
            throw new GuliException(20001,"手机号或密码为空");
        }

        //3.如果两者都不为空,则判断是否可以根据手机号查询
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);

        //4.执行查询
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);

        //5.判断当前查询对象是否为空
        if(mobileMember==null){
            throw new GuliException(20001,"登录失败...");
        }

        //6.获取当前查询对象的密码进行判断
        if(!Objects.equals(MD5.encrypt(password), mobileMember.getPassword())){
            throw new GuliException(20001,"登录密码输入错误...");
        }

        //7.判断用户是否禁用
        if(mobileMember.getIsDisabled()){
            throw new GuliException(20001,"抱歉,账户已被禁用...");
        }

        //7.登录成功,设置jwt的token
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    //2.用户注册功能
    @Override
    public void register(RegisterVo registerVo) {
        //1.获取当前注册的数据
        String code = registerVo.getCode(); //手机验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickName = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword();   //登录密码

        //2.判断当前数据是否存在null值
        if(code==null||mobile==null||nickName==null||password==null){
            throw new GuliException(20001,"注册失败...");
        }

        //3.判断手机号是否已注册,
        QueryWrapper<UcenterMember> mobileWrapper = new QueryWrapper<>();
        mobileWrapper.eq("mobile", mobile);

        //4.执行查询,判断结果
        if(baseMapper.selectCount(mobileWrapper)>0){
            throw new GuliException(20001,"注册失败,当前号码已注册...");
        }

        //5.判断验证码是否正确
        //①,根据手机号码获取redis中的验证码
        String trueCode = redisTemplate.opsForValue().get(mobile);
        //②.判断
        if(!trueCode.equals(code)){
            throw new GuliException(20001,"验证码输入错误...");
        }

        //6.使用工具类转换为实体类
        UcenterMember user = new UcenterMember();
        BeanUtils.copyProperties(registerVo,user);

        //MD5
        String realPassword = MD5.encrypt(password);
        user.setPassword(realPassword);
        user.setIsDisabled(false);
        user.setAvatar("https://kong-guli-9-5.oss-cn-beijing.aliyuncs.com/2020/09/12/476b0e1f4cf64550bdc3e0f598e61496file.png");

        //7.执行注册操作
        baseMapper.insert(user);
    }

    //根据微信用户唯一的openid判断数据库中是否存在该用户
    @Override
    public UcenterMember  selectUMemberByOpenId(String userOpenId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", userOpenId);
        return baseMapper.selectOne(wrapper);
    }
}
