package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源表
 * @TableName resources
 */
@TableName(value ="resources")
@Data
public class Resources implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 上传时间
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
     * 所属视频
     */
    private Integer vid;

    /**
     * 所属用户
     */
    private Integer uid;

    /**
     * 分P使用的标题
     */
    private String title;

    /**
     * 视频链接
     */
    private String url;

    /**
     * 原始mp4链接
     */
    private String originalUrl;

    /**
     * 视频时长
     */
    private Double duration;

    /**
     * 审核状态
     */
    private Integer status;

    /**
     * 视频最大质量
     */
    private Integer quality;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}