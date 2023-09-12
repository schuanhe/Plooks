package com.schuanhe.plooks.controller.Admin;


import com.schuanhe.plooks.domain.Message;
import com.schuanhe.plooks.service.Admin.AnnouncesAdminService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base-url}/announce/admin")
public class AnnouncesAdminController {

    @Autowired
    private AnnouncesAdminService announcesAdminService;

    /**
     * 新增公告
     */
    @PostMapping
    public ResponseResult<?> addAnnounce(@RequestBody Message.Announces announces) {
        try {
            announcesAdminService.addAnnounce(announces);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteAnnounce(@PathVariable Integer id) {
        announcesAdminService.deleteAnnounce(id);
        return ResponseResult.success();
    }

}
