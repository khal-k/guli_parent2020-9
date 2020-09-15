package com.atguigu.msm.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msm.service.MsmService;
import com.atguigu.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 孔佳齐丶
 * @create 2020-09-13 10:45
 * @package com.atguigu.msm.controller
 */
@RestController
@RequestMapping("edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //1.发送短信
    @GetMapping("sendMessage/{phone}")
    public R sendMessage(@PathVariable("phone") String phone){
        //0.先判断在Redis中是否存在手机号对应的验证码
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            //存在,则返回成功
            return R.ok();
        }
        //如果不存在,则生成并发送
        //1.使用工具列随机生成验证码
        code = RandomUtil.getFourBitRandom();
        //2.封装入map集合中
        Map<String ,Object> param = new HashMap();
        param.put("code", code);
        //3.调用方法发送短信
        boolean isSend = msmService.send(param,phone);
        //4.判断返回结果
        if(isSend){
            //5.发送成功,则将手机号对应的验证码存入redis中,并设置5分钟的生命时长
            redisTemplate.opsForValue().set(phone,code ,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().data("message","发送短信失败...");
        }
    }
}
