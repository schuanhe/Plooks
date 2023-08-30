package com.schuanhe.plooks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Archive;

import java.util.List;
import java.util.Map;

public interface ArchiveService extends IService<Archive> {
    Map<String, Integer> getArchive(int vidInt);

    Boolean hasLike(int vid);

    Boolean hasCollect(int vid);

    void addLike(int vid);

    void cancelLike(int vid);
}
