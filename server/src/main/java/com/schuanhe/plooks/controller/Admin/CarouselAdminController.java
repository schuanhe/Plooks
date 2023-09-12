package com.schuanhe.plooks.controller.Admin;


import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.service.Admin.CarouselAdminService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 轮播图
 */
@RestController
@RequestMapping("${base-url}/carousel/admin")
public class CarouselAdminController {

    @Autowired
    private CarouselAdminService carouselAdminService;

    /**
     * 新增轮播图
     */
    @PostMapping
    public ResponseResult<?> addCarousel(@RequestBody Carousels carousels) {
        carouselAdminService.addCarousel(carousels);
        return ResponseResult.success();
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteCarousel(@PathVariable Integer id) {
        carouselAdminService.deleteCarousel(id);
        return ResponseResult.success();
    }

}
