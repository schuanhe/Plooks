# Plooks

plooks是一款用户自由上传视频，并且可以使用一起看功能的视频网站。

本项目采用前后端分离的开发模式，前端使用vue.js，后端使用springboot。

项目预览地址
http://plooks.schuanhe.com

## 最近计划

- [ ] 部署文档与官网
- [ ] docker部署
- [ ] 更多建议与bug修复

## 目录结构

### client

该目录为前端vue项目，其中packages目录下分为用户端和管理端

### server

该目录为后端(api)使用springboot构建

### docs
文档目录

存放sql文件，注意数据库版本必须大于7.0，因为使用了json字段
账号:admin
密码:123456

## 打包教程

### 后端(server)

请先将**server/src/main/resources/application.yml**填写完整

完整后直接使用maven打包

```bash
mvn package
```

### 前端

前端分为manage-client和web-client

请先对 **client\utils\src\config.ts** 进行配置

```bash
# 进入前端
```bash
cd client

# 使用pnpm安装依赖，也可以使用npm

pnpm install

# 打包manage-client
cd packages\manage-client

pnpm run build

# 打包web-client
cd ..\web-client

pnpm run build
```
## 部署

### 后端

后端使用springboot打包后，直接使用java -jar运行即可

端口为2023

### 前端

前端打包后，将会有两个文件夹，manage和web

manage为管理端，web为用户端

使用nginx部署即可

nginx配置文件如下

```nginx
server {
    listen       80;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html/web;
        index  index.html index.htm;
    }

    location /manage {
        root   /usr/share/nginx/html/manage;
        index  index.html index.htm;
    }
}
```



