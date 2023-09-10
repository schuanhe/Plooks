package com.schuanhe.plooks.mapper;

import com.schuanhe.plooks.domain.Whispers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author ASUS
* @description 针对表【whispers(私信表)】的数据库操作Mapper
* @createDate 2023-09-10 09:59:48
* @Entity com.schuanhe.plooks.domain.Whispers
*/
public interface WhispersMapper extends BaseMapper<Whispers> {

    List<Whispers> getWhisperInfo(Integer fid, Integer uid, int i, Integer size);
}




