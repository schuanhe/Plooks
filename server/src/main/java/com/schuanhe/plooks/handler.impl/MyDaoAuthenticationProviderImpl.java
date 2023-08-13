package com.schuanhe.plooks.handler.impl;

import com.schuanhe.plooks.service.impl.UserDetailsServiceImpl;
import com.schuanhe.plooks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义认证逻辑
 * 添加密码不匹配会在redis记录的逻辑
 */

@Component
public class MyDaoAuthenticationProviderImpl extends DaoAuthenticationProvider {


    @Autowired
    private RedisCache redisCache;

    public MyDaoAuthenticationProviderImpl(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        super();
        setPasswordEncoder(passwordEncoder);
        // 这个地方一定要对userDetailsService赋值，不然userDetailsService是null (这个坑有点深)
        setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            //调用父类方法
            super.additionalAuthenticationChecks(userDetails, authentication);

        }catch (BadCredentialsException e){
            // 如果是邮箱登录那么就是密码对应密码(没有加密)
            if (userDetails.getPassword().equals(authentication.getCredentials().toString()))
                return;
            redisCache.increment("user:login:error:" + userDetails.getUsername(),1);
            throw new BadCredentialsException("账户或密码错误");
        }


    }
}
