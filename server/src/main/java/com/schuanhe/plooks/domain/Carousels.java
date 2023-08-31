package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 轮播图表
 * @TableName carousels
 */
@TableName(value ="carousels")
@Data
public class Carousels implements Serializable {
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
     * 图片链接
     */
    private String img;

    /**
     * 标题
     */
    private String title;

    /**
     * 指向的链接
     */
    private String url;

    /**
     * 主题色
     */
    private String color;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}