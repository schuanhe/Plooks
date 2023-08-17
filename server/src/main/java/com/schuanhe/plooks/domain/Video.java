package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频表
 * @TableName videos
 */
@TableName(value ="videos")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Video implements Serializable {
    /**
     * 
     */
    @JsonProperty(value = "vid")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 删除时间
     */
    private Date deletedAt;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 视频简介
     */
    private String desc;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 是否为原创
     */
    private Boolean copyright;

    /**
     * 点击量
     */
    private Long clicks;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 分区ID
     */
    @TableField(value = "partition_id")
    private Integer partition;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}