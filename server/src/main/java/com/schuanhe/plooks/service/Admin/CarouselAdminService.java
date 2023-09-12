package com.schuanhe.plooks.service.Admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Carousels;

public interface CarouselAdminService extends IService<Carousels> {
    void addCarousel(Carousels carousels);

    void deleteCarousel(Integer id);
}
