package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Carousels;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【carousels(轮播图表)】的数据库操作Service
* @createDate 2023-08-31 19:32:07
*/
public interface CarouselsService extends IService<Carousels> {

    List<Carousels> getCarousels();
}
