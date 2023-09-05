package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 弹幕表
 * @TableName danmukus
 */
@TableName(value ="danmukus")
@Data
public class Danmukus implements Serializable {
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
     * 视频ID
     */
    private Integer vid;

    /**
     * 分集ID
     */
    private Integer part;

    /**
     * 内容
     */
    private String text;

    /**
     * 时间
     */
    private Integer time;

    /**
     * 类型0滚动;1顶部;2底部
     */
    private Integer mode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 用户ID
     */
    private Integer uid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}