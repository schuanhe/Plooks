package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.domain.Histories;
import com.schuanhe.plooks.service.HistoriesService;
import com.schuanhe.plooks.utils.ResponseResult;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 历史记录
 */
@RestController
@RequestMapping("${base-url}/history")
public class HistoriesController {

    @Autowired
    private HistoriesService historiesService;

    @PostMapping()
    public ResponseResult<?> addHistory(@RequestBody Histories histories) {

        Integer userId = WebUtils.getUserId();
        histories.setUid(userId);

        historiesService.addHistory(histories);

        return ResponseResult.success();
    }

    /**
     * 获取历史记录列表
     * @param size 每页数量
     * @param page 页码
     * @return 历史记录列表
     */
    @GetMapping("/list/{size}/{page}")
    public ResponseResult<?> getHistoryList(@PathVariable String size, @PathVariable String page) {
        int sizeInt;
        int pageInt;
        try {
            sizeInt = Integer.parseInt(size);
            pageInt = Integer.parseInt(page);
        }catch (NumberFormatException e) {
            return ResponseResult.fail("参数错误");
        }

        Integer userId = WebUtils.getUserId();

        List<Histories> historyList = historiesService.getHistoryList(sizeInt, pageInt, userId);

        Map<String,List<Histories>> data = new HashMap<>();
        data.put("history",historyList);

        return ResponseResult.success(data);
    }

    /**
     * 获取历史视频进度
     * @param vid 视频id
     * @return 历史记录
     */
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
