# 后端部署

:::tip
 后端使用springboot开发，需要安装好java,Maven环境，数据库使用mysql,redis需要安装好mysql和redis。其中mysql的sql文件位于`docs/data/sql`中
:::

### 部署前准备

1. 该项目使用了七牛云的对象存储，需要在七牛云上创建一个对象存储空间
2. 该项目使用了邮箱验证码功能，需要在邮箱中开启smtp服务，获取邮箱的授权码

### 配置后端文件
打开 `sercver/src/main/resources/application.yml`文件，根据其中的注释修改配置文件并保存

### 打包

```bash
# 进入server目录
cd server
# 打包
mvn package
```
### 运行
直接运行jar包
其中后端默认端口为2023，如需修改请修改`application.yml`中的`server.port`属性
```bash
# 进入server目录
cd server
# 运行jar包
java -jar target/server-1.0-SNAPSHOT.jar
```

### Docker部署
:::tip
在使用docker安装之前，需要安装好docker
docker镜像可能会更新慢
:::

直接使用docker镜像部署
```bash
docker run -e MY_MAIL_HOST=smtp.exmail.qq.com \ 
           -e MY_MAIL_USERNAME=xxx@qq.com \
           -e MY_MAIL_PASSWORD=xxxxxxxx \
           -e MY_MAIL_PORT=465 \
           -e MY_QINIU_ACCESS=xxxxxxxxxxxxxx \
           -e MY_QINIU_SECRET=xxxxxxxxxxxxxxxxxx \
           -e MY_QINIU_BUCKET=xxxx \
           -e MY_QINIU_PATH=http://xxx.xxx/ \
           -p 2023:2023 -d schuanhe/plooks-api
```
环境变量说明

| 环境变量             | 说明           |
|------------------|--------------|
| MY_MAIL_HOST     | 邮箱smtp地址     |
| MY_MAIL_USERNAME | 邮箱用户名        |
| MY_MAIL_PASSWORD | 邮箱授权码        |
| MY_MAIL_PORT     | 邮箱端口         |
| MY_QINIU_ACCESS  | 七牛云accessKey |
| MY_QINIU_SECRET  | 七牛云secretKey |
| MY_QINIU_BUCKET  | 七牛云存储空间名称    |
| MY_QINIU_PATH    | 七牛云存储空间域名    |