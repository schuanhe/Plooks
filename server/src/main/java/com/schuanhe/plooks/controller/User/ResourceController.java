package com.schuanhe.plooks.controller.User;


import com.schuanhe.plooks.domain.Resources;
import com.schuanhe.plooks.service.User.ResourcesService;
import com.schuanhe.plooks.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 修改资源标题
     */
    @PutMapping
    public ResponseResult<String> updateResourceTitle(@RequestBody Resources resources){
        // 判断id是否合法
        if ( resources == null) {
            ResponseResult.error("资源不存在");
        }

        boolean updateResourceTitle = resourcesService.updateResourceTitle(resources);

        if (updateResourceTitle) {
            return ResponseResult.success("修改成功");
        } else {
            return ResponseResult.error("修改失败");
        }
    }



}
