package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 收藏夹表
 * @TableName collections
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value ="collections")
@Data
public class Collections implements Serializable {
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
     * 所属用户
     */
    private Integer uid;

    /**
     * 收藏夹名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 简介
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 封面
     */
    private String cover;

    /**
     * 是否公开
     */
    @TableField(value = "`open`")
    private boolean open;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}