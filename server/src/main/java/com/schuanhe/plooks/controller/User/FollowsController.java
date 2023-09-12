package com.schuanhe.plooks.controller.User;

import com.schuanhe.plooks.service.User.FollowsService;
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

    /**
     * 取消关注
     */
    @DeleteMapping("/{uid}")
    public ResponseResult<?> unfollow(@PathVariable Integer uid){
        followsService.unfollow(uid);
        return ResponseResult.success();
    }

    /**
     * 获取关注和粉丝数据
     */
    @GetMapping("/count/{uid}")
    public ResponseResult<?> getFollowCount(@PathVariable Integer uid){
        Map<String, Integer> data = followsService.getFollowCount(uid);
        return ResponseResult.success(data);
    }

    /**
     * 获取关注列表
     */
    @GetMapping("/following/{uid}/{size}/{page}")
    public ResponseResult<?> getFollowList(@PathVariable Integer uid, @PathVariable Integer size, @PathVariable Integer page){

        Map<String, Object> data = new HashMap<>();
        data.put("users", followsService.getFollowingList(uid, size, page));
        return ResponseResult.success(data);
    }

    /**
     * 获取粉丝列表
     */
    @GetMapping("/follower/{uid}/{size}/{page}")
    public ResponseResult<?> getFollowerList(@PathVariable Integer uid, @PathVariable Integer size, @PathVariable Integer page){

        Map<String, Object> data = new HashMap<>();
        data.put("users", followsService.getFollowerList(uid, size, page));
        return ResponseResult.success(data);
    }

}
