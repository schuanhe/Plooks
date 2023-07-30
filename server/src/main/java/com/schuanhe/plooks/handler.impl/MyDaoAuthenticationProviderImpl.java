package com.schuanhe.plooks.handler.impl;

import com.schuanhe.plooks.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyDaoAuthenticationProviderImpl extends DaoAuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public MyDaoAuthenticationProviderImpl() {
        // 在构造方法中设置UserDetailsService
        setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //try {
        //    if (authentication.getCredentials() == null) {
        //        this.logger.debug("Failed to authenticate since no credentials provided");
        //        throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        //    } else {
        //        String presentedPassword = authentication.getCredentials().toString();
        //        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
        //            this.logger.debug("Failed to authenticate since password does not match stored value");
        //            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        //        }
        //    }
        //}catch (BadCredentialsException e){
        //
        //    System.out.println("密码错误配置");
        //    throw new BadCredentialsException("用户名或密码错误");
        //}

    }
}
