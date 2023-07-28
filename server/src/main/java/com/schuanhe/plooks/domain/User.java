package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
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
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 空间封面
     */
    private String spaceCover;

    /**
     * 性别:0未知、1男、3女
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 账号状态:0正常、1停用、2删除
     */
    private String status;

    /**
     * 角色身份:0用户、1审核、2管理、3超管
     */
    private Integer role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}