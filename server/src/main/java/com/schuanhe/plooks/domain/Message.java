package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class Message {

    @TableName(value ="announces")
    @Data
    public static class Announces implements Serializable {

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private String title;

        private String content;

        private String url;

        private Integer important;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }
    @TableName(value ="at_messages")
    @Data
    public static class AtMessages implements Serializable {

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid;

        private Integer uid;

        private Integer fid;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }

    @TableName(value ="like_messages")
    @Data
    public static class LikeMessages implements Serializable {

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid;

        private Integer uid;

        private Integer fid;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }

    @TableName(value ="reply_messages")
    @Data
    public static class ReplyMessages implements Serializable {

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid;

        private Integer uid;

        private Integer fid;

        private String content;

        private String targetReplyContent;

        private String rootContent;

        private String commentId;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }
}
