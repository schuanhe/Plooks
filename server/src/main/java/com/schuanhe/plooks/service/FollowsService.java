package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Follows;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【follows(关注表)】的数据库操作Service
* @createDate 2023-09-01 19:47:34
*/
public interface FollowsService extends IService<Follows> {

    boolean getFollowStatus(Integer fid);

    void follow(Integer uid);
}
