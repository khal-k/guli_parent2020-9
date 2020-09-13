package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-12
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    //1.分页查询
    @GetMapping("bannerPage/{page}/{limit}")
    public R bannerPage(@PathVariable("page") long page,@PathVariable("limit") long limit){
        //1.新建page对象
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        //2.查询
        IPage<CrmBanner> bannerIPage = crmBannerService.page(pageBanner, null);
        //3.获取集合和总数
        List<CrmBanner> records = bannerIPage.getRecords();
        long total = bannerIPage.getTotal();

        return R.ok().data("bannerPage",records).data("total",total);
    }

    //2.根据id查询数据
    @GetMapping("getBannerAdminById/{id}")
    public R getBannerAdminById(@PathVariable("id") long id){
        //根据id查询数据
        CrmBanner byId = crmBannerService.getById(id);
        return R.ok().data("banner",byId);
    }

    //3.修改
    @PostMapping("updateBannerAdmin")
    public R updateBannerAdmin(@RequestBody CrmBanner crmBanner){
        //使用service修改
        boolean b = crmBannerService.updateById(crmBanner);
        if(b){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //4.增加
    @PostMapping("addBannerAdmin")
    public R addBannerAdmin(@RequestBody CrmBanner crmBanner){
        //增加banner
        boolean save = crmBannerService.save(crmBanner);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //5.删除
    @DeleteMapping("deleteBannerAdmin/{id}")
    public R deleteBannerAdmin(@PathVariable("id") long id){
        crmBannerService.removeById(id);
        return R.ok();
    }

}

