package com.schuanhe.plooks.handler.impl;

import com.alibaba.fastjson.JSON;
import com.schuanhe.plooks.utils.RedisCache;
import com.schuanhe.plooks.utils.ResponseResult;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //认证异常处理
        ResponseResult<String> result =ResponseResult.error(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());

        // 写进redis中
        Integer username = redisCache.getCacheObject("user:login:error:" + request.getParameter("username"));
        if (username == null){
            redisCache.setCacheObject("user:login:error:" + request.getParameter("username"), String.valueOf(1),60*5);
        }else {
            redisCache.increment("user:login:error:" + request.getParameter("username"),1);
        }

        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}