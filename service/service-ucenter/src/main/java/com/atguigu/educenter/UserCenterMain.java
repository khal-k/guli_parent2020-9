package com.atguigu.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 孔佳齐丶
 * @create 2020-09-13 14:40
 * @package com.atguigu.eduuser
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
@MapperScan("com.atguigu.educenter.mapper")
public class UserCenterMain {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterMain.class, args);
    }
}
