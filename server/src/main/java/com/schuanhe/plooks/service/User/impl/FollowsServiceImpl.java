package com.schuanhe.plooks.service.User.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Follows;
import com.schuanhe.plooks.domain.User;
import com.schuanhe.plooks.service.User.FollowsService;
import com.schuanhe.plooks.mapper.FollowsMapper;
import com.schuanhe.plooks.service.User.UserService;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【follows(关注表)】的数据库操作Service实现
* @createDate 2023-09-01 19:47:34
*/
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows>
    implements FollowsService{

    @Autowired
    private UserService userService;

    @Override
    public boolean getFollowStatus(Integer fid) {
        Integer uid = WebUtils.getUserId();

        Follows follows = baseMapper.selectOne(new QueryWrapper<Follows>().eq("uid", uid).eq("fid", fid));

        return follows != null;
    }

    @Override
    public void follow(Integer uid) {
        Integer myUid = WebUtils.getUserId();

        Follows follows = new Follows();
        follows.setUid(myUid);
        follows.setFid(uid);
        assert myUid != null;
        if (myUid.equals(uid)) {
            return;
        }

        baseMapper.insert(follows);
    }

    @Override
    public void unfollow(Integer uid) {
        Integer myUid = WebUtils.getUserId();

        assert myUid != null;
        if (myUid.equals(uid)) {
            return;
        }

        baseMapper.delete(new QueryWrapper<Follows>().eq("uid", myUid).eq("fid", uid));
    }

    @Override
    public Map<String, Integer> getFollowCount(Integer uid) {

        Integer following = Math.toIntExact(baseMapper.selectCount(new QueryWrapper<Follows>().eq("uid", uid)));
        Integer followers = Math.toIntExact(baseMapper.selectCount(new QueryWrapper<Follows>().eq("fid", uid)));

        Map<String, Integer> data = new HashMap<>();
        data.put("following", following);
        data.put("follower", followers);

        return data;
    }

    @Override
    public List<User> getFollowingList(Integer uid, Integer size, Integer page) {
        Page<Follows> pages = new Page<>(page, size);
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<Follows>().eq("uid", uid).orderByDesc("created_at");
        List<User> data = new ArrayList<>();
        Page<Follows> followsPage = baseMapper.selectPage(pages, queryWrapper);
        // 获取关注者信息
        followsPage.getRecords().forEach(follows -> {
            User userInfo = userService.getUserInfoById(follows.getFid());
            data.add(userInfo);

        });
        return data;
    }

    @Override
    public List<User> getFollowerList(Integer uid, Integer size, Integer page) {
        Page<Follows> pages = new Page<>(page, size);
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<Follows>().eq("fid", uid).orderByDesc("created_at");
        Page<Follows> followsPage = baseMapper.selectPage(pages, queryWrapper);
        List<User> data = new ArrayList<>();
        // 获取关注者信息
        followsPage.getRecords().forEach(follows -> {
            User userInfo = userService.getUserInfoById(follows.getUid());
            data.add(userInfo);

        });
        return data;
    }
}




