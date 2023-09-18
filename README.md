# Plooks

plooks是一款用户自由上传视频，并且可以使用一起看功能的视频网站。

本项目采用前后端分离的开发模式，前端使用vue.js，后端使用springboot。

项目为毕业设计

## 目录结构

### client

该目录为前端vue项目，其中packages目录下分为用户端和管理端

### server

该目录为后端(api)使用springboot构建

### sql

存放sql文件，注意数据库版本必须大于7.0，因为使用了json字段



## 打包

前端两个都使用pnpm 打包与下载依赖

后端使用maven

