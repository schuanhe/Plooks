# 常见问题解答

## 后台管理404
后台管理地址为 `域名/manage/`，最后以`/`结尾，没有`/`可能会出现404

## sql文件导入失败
sql文件中有字段使用json格式，需要版本大于5.7.8，如果版本低于5.7.8，可以将json字段改为text

## 修改默认管理员密码
使用默认账号(账号：schuanhe 密码: 123456)登录后台管理，
修改邮箱为自己的邮箱，进入到前端登录页面，点击找回密码修改密码

## 修改网站配色
找到主题文件
- web端 `web\packages\web-client\src\theme\index.ts`
- 后台管理端 `web\packages\manage-client\src\theme\index.ts`

修改以下颜色并重新打包
```js
const defaultTheme: Theme = {
    primaryColor: "#ffc90c",
    primaryHoverColor: "#f8df72" 
}
```
## 修改后台管理路径
后台管理默认`/manage/`,如需修改须按照以下步骤：
1. 打开`web\packages\manage-client\vite.config.ts`，修改`base`为想要的路径，
（像`/manage`前面有`/`后面没有`/`），修改`build.outDir`，需要与`base`相对应（没有`/`）
2. 配置nginx时，找到以下部分内容，并将`manage`替换掉。
```
location /manage/ { 
    root /usr/share/nginx/html;
    index index.html index.htm;
    try_files $uri $uri/ @manage;
}

# 解决后台管理history路由问题
location @manage {
    rewrite ^.*$ /manage/index.html;
}
```


