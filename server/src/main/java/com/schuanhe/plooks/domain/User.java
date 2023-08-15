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
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class User implements Serializable {
    /**
     *  主键
     */

    @JsonProperty(value = "uid")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *  创建时间
     */
    private Date createdAt;

    /**
     *  更新时间
     */
    private Date updatedAt;

    /**
     *  删除时间
     */
    private Date deletedAt;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

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
     * 角色身份:0 用户、1审核、2管理、3超管
     */
    private Integer role;

    /**
     * 邮箱验证码
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}