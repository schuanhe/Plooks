package com.schuanhe.plooks.controller;

import com.schuanhe.plooks.service.FollowsService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
