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
 * 分区表
 * @TableName partitions
 */
@TableName(value ="partitions")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Partitions implements Serializable {
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
     * 分区名称
     */
    private String content;

    /**
     * 所属分区ID
     */
    private Integer parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}