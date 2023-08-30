package com.schuanhe.plooks.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;


public class Archive {

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Collect{

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid;

        private Integer cid;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Like{

        @TableId(type = IdType.AUTO)
        private Integer id;

        private Date createdAt;

        private Date updatedAt;

        private Date deletedAt;

        private Integer vid;

        private Integer uid;

        @TableField(exist = false)
        private static final long serialVersionUID = 1L;
    }

}
