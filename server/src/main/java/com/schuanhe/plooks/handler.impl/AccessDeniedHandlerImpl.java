package com.schuanhe.plooks.handler.impl;

import com.alibaba.fastjson.JSON;
import com.schuanhe.plooks.utils.ResponseResult;
import com.schuanhe.plooks.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //授权处理异常
        ResponseResult<String> result = ResponseResult.error(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
        //
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
