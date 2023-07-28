package com.schuanhe.plooks.config;

import com.schuanhe.plooks.filter.JwtAuthenticationTokenFilter;
import com.schuanhe.plooks.handler.impl.AccessDeniedHandlerImpl;
import com.schuanhe.plooks.handler.impl.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author SCHuanHe https://github.com/schuanhe
 */
//@EnableWebSecurity //由于某种原因，IDE 无法检测到HttpSecurities 是由 Spring Boot 配置的。您可以通过添加到您的配置类来消除错误@EnableWebSecurity，它解决了它，因为注释导入了HttpSecurityConfiguration配置类。
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //配置认证错误处理器
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    //配置权限错误处理器
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                // 对于获取验证码接口 允许匿名访问
                .antMatchers("/user/captchaImage").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        //在xxx过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                //配置认证异常处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                //配置授权异常处理器
                .accessDeniedHandler(accessDeniedHandler);
        //允许跨域
        http.cors();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}