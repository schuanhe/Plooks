package com.schuanhe.plooks.service.User;

import com.schuanhe.plooks.domain.Follows;
import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.User;

import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【follows(关注表)】的数据库操作Service
* @createDate 2023-09-01 19:47:34
*/
public interface FollowsService extends IService<Follows> {

    boolean getFollowStatus(Integer fid);

    void follow(Integer uid);

    void unfollow(Integer uid);

    Map<String, Integer> getFollowCount(Integer uid);

    /**
     * 获取关注列表
     * @param uid 当前用户id
     * @param size 每页大小
     * @param page 页码
     * @return 关注列表
     */
    List<User> getFollowingList(Integer uid, Integer size, Integer page);

    /**
     * 获取粉丝列表
     * @param uid 当前用户id
     * @param size 每页大小
     * @param page 页码
     * @return 粉丝列表
     */
    List<User> getFollowerList(Integer uid, Integer size, Integer page);
}
