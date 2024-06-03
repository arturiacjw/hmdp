package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryList() {
        //1.判断redis缓存中是否存在查找信息
        String shopList = stringRedisTemplate.opsForValue().get(SHOP_TYPE_KEY);
        if(StrUtil.isNotBlank(shopList)) {
            //2.存在，则直接返回信息
            List<ShopType> lst = JSONUtil.toList(shopList, ShopType.class);
            return Result.ok(lst);
        }
        //3.不存在，则在数据库查询信息
        List<ShopType> lst = query().orderByAsc("sort").list();
        //4.判断数据库中商户信息是否存在
        if(lst==null){
            //5.不存在，则返回错误信息
            return Result.fail("信息不存在");
        }
        //6.存在，则将信息写入redis中
        stringRedisTemplate.opsForValue().set(SHOP_TYPE_KEY,JSONUtil.toJsonStr(lst),CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        //7.返回
        return Result.ok(lst);
    }
}
