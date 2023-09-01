package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

public class Comments {

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Comment {
        private int id;
        private int vid;
        private long createdAt;
        private String content;
        private int uid;
        private boolean isDelete;
        private List<Integer> at;
        private boolean noMore; // 是否还有更多
        @TableField(exist = false)
        private List<Reply> reply;
        @TableField(exist = false)
        private User author;

    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class Reply {
        private int id;
        private long createdAt;
        private String content;
        private int uid;
        private int fid;
        private boolean isDelete;
        private List<Integer> at;
        @TableField(exist = false)
        private User author;
    }
}
