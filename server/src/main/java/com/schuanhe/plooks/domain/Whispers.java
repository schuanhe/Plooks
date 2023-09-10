package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 私信表
 * @TableName whispers
 */
@TableName(value ="whispers")
@Data
public class Whispers implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    /**
     * 
     */
    private Date deletedAt;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 关联ID
     */
    private Integer fid;

    /**
     * 发送者
     */
    private Integer fromId;

    /**
     * 接受者
     */
    private Integer toId;

    /**
     * 内容
     */
    private String content;

    /**
     * 已读状态
     */
    private Boolean status;

    /**
     * 发送者的信息
     */
    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}