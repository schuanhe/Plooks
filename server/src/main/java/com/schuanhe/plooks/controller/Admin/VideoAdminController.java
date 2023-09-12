package com.schuanhe.plooks.controller.Admin;

import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.Video;
import com.schuanhe.plooks.service.Admin.VideoAdminService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频
 */
@RestController
@RequestMapping("${base-url}/video/admin")
public class VideoAdminController {

    @Autowired
    private VideoAdminService videoAdminService;


    /**
     * 管理员获取视频列表
     */
    @GetMapping("/{size}/{page}/{partitions}")
    public ResponseResult<?> getVideoList(@PathVariable Integer page, @PathVariable Integer size, @PathVariable(required = false) Integer partitions) {
        int pid = 0 ;
        if (partitions != null)
             pid = partitions;

        Map<String, Object> data = new HashMap<>();
        data.put("total", videoAdminService.getVideoCount(pid));
        data.put("videos", videoAdminService.getVideoList(page,size,pid));

        return ResponseResult.success(data);

    }

    /**
     * 管理员搜索视频
     */
    @GetMapping("/search/{keyword}/{size}/{page}")
    public ResponseResult<?> searchVideo(@PathVariable String keyword, @PathVariable Integer page, @PathVariable Integer size) {

        List<Video> videos = videoAdminService.searchVideo(keyword,page,size);

        Long videoCount = videoAdminService.searchVideoCount(keyword);

        // 返回视频列表
        Map<String, Object> data = new HashMap<>();
        data.put("total", videoCount);
        data.put("videos", videos);
        return ResponseResult.success(data);
    }

    /**
     * 删除视频
     */
    @DeleteMapping("/{vid}")
    public ResponseResult<?> deleteVideo(@PathVariable Integer vid) {
        boolean b = videoAdminService.deleteVideo(vid);
        if (!b)
            return ResponseResult.error("删除失败");
        return ResponseResult.success();
    }

    /**
     * 获取待审核视频列表
     */
    @GetMapping("/review/{size}/{page}")
    public ResponseResult<?> getReviewVideoList(@PathVariable Integer page, @PathVariable Integer size) {

        Long reviewVideoCount = videoAdminService.getReviewVideoCount();
        List<Video> reviewVideoList = videoAdminService.getReviewVideoList(page, size);
        Map<String, Object> data = new HashMap<>();
        data.put("total", reviewVideoCount);
        data.put("videos", reviewVideoList);
        return ResponseResult.success(data);
    }

    /**
     * 获取视频资源
     */
    @GetMapping("/review/resource/{id}")
    public ResponseResult<?> getVideoResource(@PathVariable Integer id) {
        List<Resources> resources = videoAdminService.getVideoResource(id);
        Map<String, Object> data = new HashMap<>();
        data.put("resources",resources);
        return ResponseResult.success(data);
    }


    /**
     * 审核视频
     */
    @PutMapping("/review/video")
    public ResponseResult<?> reviewVideo(@RequestBody Video video) {
        if (video.getId() == null || video.getId() == 0)
            return ResponseResult.error("视频id不能为空");
        if (video.getStatus() == null)
            return ResponseResult.error("审核状态不能为空");

        boolean b = videoAdminService.reviewVideo(video);
        if (!b)
            return ResponseResult.error("审核失败");
        return ResponseResult.success();
    }

    /**
     * 审核视频资源
     */
    @PutMapping("/review/resource")
    public ResponseResult<?> reviewVideoResource(@RequestBody Resources resources) {
        if (resources.getId() == null || resources.getId() == 0)
            return ResponseResult.error("资源id不能为空");
        if (resources.getStatus() == null)
            return ResponseResult.error("审核状态不能为空");

        boolean b = videoAdminService.reviewVideoResource(resources);
        if (!b)
            return ResponseResult.error("审核失败");
        return ResponseResult.success();
    }


}
