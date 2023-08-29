package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.domain.Histories;
import com.schuanhe.plooks.service.HistoriesService;
import com.schuanhe.plooks.utils.ResponseResult;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 历史记录
 */
@RestController
@RequestMapping("${base-url}/history")
public class HistoriesController {

    @Autowired
    private HistoriesService historiesService;

    @GetMapping("/progress/{vid}")
    public ResponseResult<?> getProgressByVid(@PathVariable String vid) {
        int vidInt;
        try {
            vidInt = Integer.parseInt(vid);
        }catch (NumberFormatException e) {
            return ResponseResult.fail("vid格式错误");
        }

        Integer userId = WebUtils.getUserId();

        Histories histories = historiesService.getProgressByVid(vidInt,userId);
        // 去除不必要的字段
        histories.setUid(null);
        histories.setVid(null);

        Map<String,Histories> data = new HashMap<>();
        data.put("progress",histories);

        return ResponseResult.success(data);

    }
}
