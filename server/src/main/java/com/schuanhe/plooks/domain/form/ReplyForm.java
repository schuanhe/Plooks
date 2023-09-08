package com.schuanhe.plooks.domain.form;


import lombok.Data;

import java.util.List;

/**
 * 接受回复参数
 */
@Data
public class ReplyForm {

    private Integer vid; //视频id
    private String content; //回复内容
    private Integer parentId; //父评论id

    private Integer replyUserId; //回复对象id

    private String replyContent; // 回复评论
    private List<String> at; // @用户昵称列表

}
