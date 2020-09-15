package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.util.ConstantPropertiesUtil;
import com.atguigu.educenter.util.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
/**
 * @author 孔佳齐丶
 * @create 2020-09-14 18:49
 * @package com.atguigu.educenter.controller
 */
@Slf4j
@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/educenter/api/ucenter/wx")
public class WxApiController {

    private Logger logger = LoggerFactory.getLogger(WxApiController.class);

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //返回扫描人信息
    @GetMapping("callback")
    public String callBack(String code,String state){
        try {
            //1.获取code值,一个临时票据
            //logger.info(code+state);

            //2.拿着code值获取到微信请求固定地址,access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);

            //请求这个凭借好的地址,返回得到的两个值access_token 和 openid
            //使用httpclient发送请求,得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            /*"access_token":"37_FqIIEsUDLm3veKsollZbwtmfi6f2kCBr9 -TYZeUMSDPRopngsETgI41crR60sylxDtRjm5f08JIetRnIVwvwLYLIfLM7SNRNfeb_ALpr-3A",

            "expires_in":7200,

            "refresh_token":"37_K8HoWFujOEMFmg_xetHtFcQS0UDqv8UUtnTD_ZbORtFc_5-aSmxXUzxpcUi86eF_gESoZ4hu2z2Y7SKNLscfKY2ZeVc9cnmjASflLoWaU6w",

            "openid":"o3_SC52D0dGguiCVE6tHoO4buyvc",

            "scope":"snsapi_login",

            "unionid":"oWgGz1KuR97psQ6ofcjErDVNugAs"}*/
            //System.out.println("accessTokenInfo==========="+accessTokenInfo);

            //从accessTokenInfo字符串取出两个值,access_token和openid
            //把从accessTokenInfo字符串转换map集合,根据map里面的key获取对应值
            //使用json转换工具GSON
            Gson  gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            //根据map属性获取access_token和openid
            String openid = (String)mapAccessToken.get("openid");
            String access_token = (String) mapAccessToken.get("access_token");


            //7.根据微信用户唯一的openid判断数据库中是否存在该用户
            UcenterMember ucenterMember = ucenterMemberService.selectUMemberByOpenId(openid);
            if(ucenterMember==null){
                //3.拿着两个值再起微信固定地址获取扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //4.再次拼接地址
                String baseUserInfo = String.format(baseUserInfoUrl,
                        access_token,
                        openid);
                //5.再次使用httpclient发送请求,获取扫描人信息
                String userInfo = HttpClientUtils.get(baseUserInfo);

                /*
                "openid":"o3_SC52D0dGguiCVE6tHoO4buyvc",

                "nickname":"~~~","

                sex":1,

                "language":"zh_CN",

                "city":"Shenyang",

                "province":"Liaoning",

                "country":"CN",

                "headimgurl":"https:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/Q0j4TwGTfTLjczEniatkHaiacBYpXOQwoeoaibbuPu4Na62vg4I9QHG4yvFc7HbgXd3ibsxNibkibecmI9yp8ibicuIyhw\/132"

                ,"privilege":[],

                "unionid":"oWgGz1KuR97psQ6ofcjErDVNugAs"}
                 */

                //6.使用json转换工具GSON,把userInfo转换为map类型的集合
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");
                String userOpenId = (String) userInfoMap.get("openid");

                //8.如果符合条件,将客户存于数据库
                ucenterMember = new UcenterMember();
                ucenterMember.setAvatar(headimgurl);
                ucenterMember.setNickname(nickname);
                ucenterMember.setOpenid(userOpenId);
                //9.执行
                ucenterMemberService.save(ucenterMember);
            }

            //10.使用jwt生成token对象,并通过路径返回
            String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"微信登录注册失败");
        }
    }

    //微信扫描返回二维码页面
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        // 微信开放平台授权baseUrl %s相当于? 占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s        " +
                "#wechat_redirect";

        //对redirect_url进行URLEncode编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        return "redirect:"+url;
    }

}
