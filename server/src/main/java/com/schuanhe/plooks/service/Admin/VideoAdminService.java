package com.schuanhe.plooks.service.Admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.domain.Video;

import java.util.List;


public interface VideoAdminService extends IService<Video> {
    List<Video> getVideoList(Integer page, Integer size, int pid);

    Long getVideoCount(int pid);

    List<Video> getReviewVideoList(Integer page, Integer size);

    Long getReviewVideoCount();

    List<Resources> getVideoResource(Integer id);

    boolean reviewVideoResource(Resources resources);

    boolean reviewVideo(Video video);

    List<Video> searchVideo(String keyword, Integer page, Integer size);

    Long searchVideoCount(String keyword);

    boolean deleteVideo(Integer vid);
}
