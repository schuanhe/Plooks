package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Whispers;
import com.schuanhe.plooks.service.UserService;
import com.schuanhe.plooks.service.WhispersService;
import com.schuanhe.plooks.mapper.WhispersMapper;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.WebSocketManager;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【whispers(私信表)】的数据库操作Service实现
* @createDate 2023-09-10 09:59:48
*/
@Service
public class WhispersServiceImpl extends ServiceImpl<WhispersMapper, Whispers>
    implements WhispersService{

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Whispers> getWhispers() {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return null;
        }
        // 最近消息列表仅通过Redis储存
        Map<String, Whispers> cacheMap = redisCache.getCacheMap("message:whispers:" + uid);
        return new ArrayList<>(cacheMap.values());

    }

    @Override
    public void readWhisper(Integer fid) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return;
        }
        // 从Redis中获取最近消息列表
        Whispers whispers = redisCache.getCacheMapValue("message:whispers:" + uid, fid.toString());
        // 获取未读消息列表
        whispers.setStatus(true);
        // 更新Redis中的最近消息列表
        redisCache.setCacheMapValue("message:whispers:" + uid, fid.toString(), whispers);
    }

    @Override
    public void sendWhisper(Whispers whispers) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return;
        }
        // 获取fid
        Integer fid = whispers.getFid();
        // 设置状态
        whispers.setStatus(false);
        // 设置发送者
        whispers.setFromId(uid);
        // 设置接收者
        whispers.setToId(fid);
        // 设置发送时间
        whispers.setCreatedAt(new Date());
        whispers.setUpdatedAt(new Date());
        // 插入数据库
        baseMapper.insert(whispers);

        // 设置用户信息
        whispers.setUser(userService.getUserInfoById(fid));
        // 新增最近消息列表
        redisCache.setCacheMapValue("message:whispers:" + uid, fid.toString(), whispers);

        whispers.setUser(userService.getUserInfoById(uid));
        redisCache.setCacheMapValue("message:whispers:" + fid, uid.toString(), whispers);

        // 发送websocket消息
        WebSocketManager.sendMessage(fid, whispers); //fid接受的是发送者的信息

    }

    @Override
    public List<Whispers> getWhisperInfo(Integer fid, Integer size, Integer page) {
        // 获取uid
        Integer uid = WebUtils.getUserId();
        if (uid == null) {
            return null;
        }

        List<Whispers> whisperInfo = baseMapper.getWhisperInfo(fid, uid, (page - 1) * size, size);
        // 将数据倒叙
        List<Whispers> whispers = new ArrayList<>();
        for (int i = whisperInfo.size() - 1; i >= 0; i--) {
            whispers.add(whisperInfo.get(i));
        }
        return whispers;

    }
}




