package com.schuanhe.plooks.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.schuanhe.plooks.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;
    private List<String> permissions;

    //不会储存到redis中
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (authorities!=null){
            return authorities;
        }

        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority对象
        //List<SimpleGrantedAuthority> newList = new ArrayList<>();
        //for (String permission : permissions) {
        //    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
        //    newList.add(simpleGrantedAuthority);
        //}
        //比for循环更好用
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
        return authorities;

    }

    // 获取密码
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 获取用户名
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 账户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否被锁定
    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(user.getStatus(), "0");
    }

    // 密码是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否可用
    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null;
    }
}
