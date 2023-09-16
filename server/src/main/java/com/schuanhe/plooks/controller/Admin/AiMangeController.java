package com.schuanhe.plooks.controller.Admin;


import com.schuanhe.plooks.service.Admin.AiMangeService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${base-url}/aiMange")
public class AiMangeController {

    @Autowired
    private AiMangeService aiMangeService;

    /**
     * 通过问题获取数据
     */
    @PostMapping("/getAiData")
    public ResponseResult<?> getAiData(@RequestBody Map<String,String> data) {
        String q = data.get("q");
        if (q == null || "".equals(q)) {
            return ResponseResult.error("参数错误");
        }
        try {
            Map<String, Object> aiData = aiMangeService.getAiData(q);
            return ResponseResult.success(aiData);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }


    }

}
