package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 关注表
 * @TableName follows
 */
@TableName(value ="follows")
@Data
public class Follows implements Serializable {
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
     * 关注的用户ID
     */
    private Integer fid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}