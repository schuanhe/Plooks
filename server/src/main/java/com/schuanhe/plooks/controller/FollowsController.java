package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.service.FollowsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  粉丝关注接口
 */
@RestController
@RequestMapping("${base-url}/follow")
public class FollowsController {

    @Autowired
    private FollowsService followsService;

    /**
     * 关注状态
     */
    @GetMapping("/status/{fid}")
    public ResponseResult<?> getFollowStatus(@PathVariable Integer fid){
        boolean follow = followsService.getFollowStatus(fid);

        Map<String, Boolean> data = new HashMap<>();
        data.put("follow", follow);
        return ResponseResult.success(data);
    }

    /**
     * 关注
     */
    @PostMapping("/{uid}")
    public ResponseResult<?> follow(@PathVariable Integer uid){
        followsService.follow(uid);
        return ResponseResult.success();
    }
}
