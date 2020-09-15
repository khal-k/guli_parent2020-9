package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 孔佳齐丶
 * @create 2020-09-11 20:42
 * @package com.atguigu.eduservice.client
 */
@Component
public class VodClientFallBack implements VodClient{
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除单个视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
