package com.hmdp.controller;


import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private IShopTypeService typeService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("list")
    public Result queryTypeList() {
        String key="cache:shopType:list";
        String type=stringRedisTemplate.opsForValue().get(key);
        List<ShopType> typeList = JSONUtil.toList(type,ShopType.class);
        if(type!=null){
            return Result.ok(typeList);
        }
        //缓存中没有
        typeList = typeService
                .query().orderByAsc("sort").list();
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(typeList));
        return Result.ok(typeList);
    }
}
