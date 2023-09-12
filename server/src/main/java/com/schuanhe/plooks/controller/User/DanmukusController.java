package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.domain.Danmukus;
import com.schuanhe.plooks.service.User.DanmukusService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${base-url}/danmuku")
public class DanmukusController {

    @Autowired
    private DanmukusService danmukusService;

    /**
     * 获取弹幕
     */
    @GetMapping("/{vid}/{part}")
    public ResponseResult<?> getDanmukus(@PathVariable Integer vid, @PathVariable Integer part) {

        List<Danmukus> danmukusList = danmukusService.getDanmukus(vid,part);

        Map<String,List<Danmukus>> data = new HashMap<>();
        data.put("Danmuku",danmukusList);

        return ResponseResult.success(data);

    }

    /**
     * 添加弹幕
     */
    @PostMapping
    public ResponseResult<?> addDanmukus(@RequestBody Danmukus danmukus) {

        danmukusService.addDanmukus(danmukus);

        return ResponseResult.success();
    }

}
