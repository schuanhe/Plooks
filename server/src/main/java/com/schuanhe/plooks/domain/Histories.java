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
 * 历史记录表
 * @TableName histories
 */
@TableName(value ="histories")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Histories implements Serializable {
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
     * 所在视频id
     */
    private Integer vid;

    /**
     * 所属用户ID
     */
    private Integer uid;

    /**
     * 分集
     */
    private Integer part;

    /**
     * 进度
     */
    private Double time;

    /**
     * 视频信息
     */
    @TableField(exist = false)
    private Video video;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}