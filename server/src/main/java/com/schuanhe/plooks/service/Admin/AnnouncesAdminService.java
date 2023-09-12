package com.schuanhe.plooks.service.Admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schuanhe.plooks.domain.Carousels;
import com.schuanhe.plooks.domain.Message;

public interface AnnouncesAdminService extends IService<Message> {
    void addAnnounce(Message.Announces announces) throws Exception;

    void deleteAnnounce(Integer id);
}
