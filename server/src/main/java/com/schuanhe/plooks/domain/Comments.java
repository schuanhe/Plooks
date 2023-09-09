package com.schuanhe.plooks.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

public class Comments {

    @Data
    @Accessors(chain = true)    // 允许自定义设置链式调用
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Comment {

        @TableId(type = IdType.AUTO)
        private int id; // 评论id

        private int vid; // 视频id

        private Date createdAt; // 创建时间

        private String content; // 内容

        private int uid; // 用户id


        private boolean isDelete; // 是否删除

        @TableField(exist = false) // 不映射到数据库
        private List<String> at;

        @TableField(value = "at") // 映射到数据库
        private String atStr;

        private boolean noMore; // 是否还有更多

        @TableField(exist = false)
        private List<Reply> reply; // 回复列表

        @TableField(exist = false)
        private User author;


        public void setAt(List<String> at) {
            this.at = at;
            this.atStr = JSON.toJSONString(at);
        }

        public void setAtStr(String atStr) {
            this.atStr = atStr;
            this.at = JSON.parseArray(atStr, String.class);
        }
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @Accessors(chain = true)    // 允许自定义设置链式调用
    public static class Reply {
        @TableId(type = IdType.AUTO)
        private int id;
        private Date createdAt;
        private String content;
        private int uid;
        private int fid;
        private int vid;
        private boolean isDelete;
        @TableField(exist = false) // 不映射到数据库
        private List<Integer> at;
        @TableField(value = "at") // 映射到数据库
        private String atStr;
        @TableField(exist = false)
        private User author;

        public void setAt(List<Integer> at) {
            this.at = at;
            this.atStr = JSON.toJSONString(at);
        }

        public void setAtStr(String atStr) {
            this.atStr = atStr;
            this.at = JSON.parseArray(atStr, Integer.class);
        }
    }
}
