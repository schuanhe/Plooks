# 前端部署

:::tip
前端使用vue3开发，需要安装好nodejs和pnpm环境
:::

### 配置前端文件
前端文件位于`client\utils\src\config.ts`，修改`domain`为后端域名+端口，如`xxx.xxx.xxx.xxx:2023`，如果是本地部署可以使用`localhost:2023`

### 打包
前端分为web-client、manage-client两个项目，分别对应web端、后台管理端：

先安装依赖
```
# 进入client目录
cd client
# 安装依赖
pnpm i
```
如果没有安装pnpm，可以使用npm代替或者安装pnpm
```
# 安装pnpm
npm i -g pnpm
```
打包web-client
```
# 进入web-client目录
cd packages/web-client
# 打包
pnpm run build
```
打包manage-client
```
# 进入manage-client目录
cd packages/manage-client
# 打包
pnpm run build
```

### 部署

项目使用可以使用nginx部署，需要先将打包好的文件复制到nginx的html目录下，然后配置nginx
nginx配置文件如下：
```
server {
    listen       80;
	server_name  localhost; #有域名可以把localhost替换为域名
	client_max_body_size 1024M;

    location / {
        root /usr/share/nginx/html/web;
        index index.html index.htm;
        try_files $uri $uri/ @web;
    }

    # 解决history路由问题
    location @web {
        rewrite ^.*$ /index.html;
    }

    #后台管理
    location /manage/ {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ @manage;
    }

    # 解决后台管理history路由问题
    location @manage {
        rewrite ^.*$ /manage/index.html;
    }

}
```