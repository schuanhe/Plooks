# 登录文档

本项目登录采用双token登录

即: access_token(12小时) 和 refresh_token(一周)
![img.png](src/token.png)

access_token 在登陆后会存在redis中`user:token:token 为key`有效时间为12小时

refresh_token 则不会存在redis中, 会存在cookie中, 有效时间为一周