package com.schuanhe.plooks.controller;


import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.service.CarouselsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮播图相关接口
 */
@RestController
@RequestMapping("${base-url}/carousel")
public class CarouselController {

    @Autowired
    private CarouselsService carouselsService;
    /**
     * 获取轮播图
     * @return 轮播图
     */
    @GetMapping
    public ResponseResult<?> getCarousel() {
        List<Carousels> carousels = carouselsService.getCarousels();

        // 返回轮播图
        Map<String, Object> data = new HashMap<>();
        data.put("carousels", carousels);
        return ResponseResult.success(data);
    }

}
