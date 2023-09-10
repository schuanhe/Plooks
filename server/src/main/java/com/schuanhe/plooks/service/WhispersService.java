package com.schuanhe.plooks.service;

import com.schuanhe.plooks.domain.Whispers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【whispers(私信表)】的数据库操作Service
* @createDate 2023-09-10 09:59:48
*/
public interface WhispersService extends IService<Whispers> {

    List<Whispers> getWhispers();

    void readWhisper(Integer fid);

    void sendWhisper(Whispers whispers);

    List<Whispers> getWhisperInfo(Integer fid, Integer size, Integer page);
}
