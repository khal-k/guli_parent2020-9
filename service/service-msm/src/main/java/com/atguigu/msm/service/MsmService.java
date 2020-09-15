package com.atguigu.msm.service;

import java.util.Map;

/**
 * @author 孔佳齐丶
 * @create 2020-09-13 10:44
 * @package com.atguigu.msm.service
 */
public interface MsmService {
    boolean send(Map<String, Object> param, String phone);
}
