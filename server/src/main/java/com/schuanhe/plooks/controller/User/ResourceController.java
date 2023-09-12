package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.service.User.ResourcesService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-url}/resource")
public class ResourceController {


    @Autowired
    private ResourcesService resourcesService;

    /**
     * 删除资源
     * @param id 资源id
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteResource(@PathVariable Integer id){
        // 判断id是否合法
        if (id == null) {
            ResponseResult.error("资源不存在");
        }

        boolean deleteResource = resourcesService.deleteResource(id);

        if (deleteResource) {
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.error("删除失败");
        }


    }

}
