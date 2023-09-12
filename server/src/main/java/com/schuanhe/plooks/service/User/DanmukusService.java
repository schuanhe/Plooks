package com.schuanhe.plooks.service.User;

import com.schuanhe.plooks.domain.Danmukus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【danmukus(弹幕表)】的数据库操作Service
* @createDate 2023-09-05 13:01:36
*/
public interface DanmukusService extends IService<Danmukus> {

    /**
     * 获取弹幕
     * @param vid 视频id
     * @param part 视频分P
     * @return 弹幕列表
     */
    List<Danmukus> getDanmukus(Integer vid, Integer part);

    void addDanmukus(Danmukus danmukus);
}
