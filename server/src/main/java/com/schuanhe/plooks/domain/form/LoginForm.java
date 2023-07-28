package com.schuanhe.plooks.domain.form;

import com.schuanhe.plooks.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    private User user; //用户信息
    private String code; //验证码
    private String uuid; //验证码唯一标识
}
