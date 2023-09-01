package com.schuanhe.plooks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schuanhe.plooks.domain.Follows;
import com.schuanhe.plooks.service.FollowsService;
import com.schuanhe.plooks.mapper.FollowsMapper;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【follows(关注表)】的数据库操作Service实现
* @createDate 2023-09-01 19:47:34
*/
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows>
    implements FollowsService{

    @Override
    public boolean getFollowStatus(Integer fid) {
        Integer uid = WebUtils.getUserId();

        Follows follows = baseMapper.selectOne(new QueryWrapper<Follows>().eq("uid", uid).eq("fid", fid));

        return follows != null;
    }
}




