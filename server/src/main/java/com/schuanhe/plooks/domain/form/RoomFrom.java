package com.schuanhe.plooks.domain.form;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * 房间配置实体类
 */
@Data
public class RoomFrom {
    private Integer id; // 房间id
    // 是否不公开
    private Boolean isPublic;
    // 密码
    private String password;
    // 房间 管理员
    private Integer adminId;
    // 在房间的用户id
    private List<Integer> userIds;
    // 房间播放视频id
    private Integer vid;
    // 分p
    private Integer pid;
    // 视频进度
    private Double time;
    // 视频是否在播放
    private Boolean isPlay;

    // 用户id与其对应的session(不序列化)
    @JsonIgnore
    private Map<Integer, Session> userSession;

}
