package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NoArgsConstructor
    public static class AtMessages implements Serializable {

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid; // 视频id

        private Integer uid; // 被@者id

        private Integer fid; // @者id

        @TableField(exist = false)
        private User user;  // @者

        @TableField(exist = false)
        private Video video;  // 视频

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;

        public AtMessages(Integer vid, Integer uid, Integer fid) {
            this.vid = vid;
            this.uid = uid;
            this.fid = fid;
            this.createdAt = new Date();
            this.updatedAt = new Date();
        }
    }

    @TableName(value ="like_messages")
    @Data
    @NoArgsConstructor
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
        private User user;  // 点赞者

        @TableField(exist = false)
        private Video video;  // 视频

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;

        public LikeMessages(Integer vid, Integer uid, Integer fid) {
            this.vid = vid;
            this.uid = uid;
            this.fid = fid;
            this.createdAt = new Date();
            this.updatedAt = new Date();
        }
    }

    @TableName(value ="reply_messages")
    @Data
    @NoArgsConstructor
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

        private Integer commentId;

        @TableField(exist = false)
        private User user;  // 评论者

        @TableField(exist = false)
        private Video video;  // 视频

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;

        /**
         * 回复 回复消息构造器
         * @param vid 视频id
         * @param uid 消息接受者用户id
         * @param fid 消息提供者uid
         * @param content 回复内容
         * @param targetReplyContent 回复评论
         * @param rootContent 根评论
         * @param commentId 评论id
         */
        public ReplyMessages(Integer vid, Integer uid, Integer fid, String content, String targetReplyContent, String rootContent, Integer commentId) {
            this.vid = vid;
            this.uid = uid;
            this.fid = fid;
            this.content = content;
            this.targetReplyContent = targetReplyContent;
            this.rootContent = rootContent;
            this.commentId = commentId;
            this.createdAt = new Date();
        }

        /**
         * 回复 评论消息构造器
         */
        public ReplyMessages(Integer vid, Integer uid, Integer fid, String content) {
            this.vid = vid;
            this.uid = uid;
            this.fid = fid;
            this.content = content;
            this.createdAt = new Date();
        }
    }
}
