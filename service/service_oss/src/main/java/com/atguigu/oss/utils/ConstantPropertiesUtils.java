package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    private String endpoint = "oss-cn-beijing.aliyuncs.com";

    private String keyId ="LTAI4GEvURKFYLQL1MApFYVN";

    private String keySecret = "9Z71vgaP7ku07LhE1znrX4R7o3cPsq";

    private String bucketName = "kong-guli-9-5";

    //定义公开静态常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
