# nginx镜像
FROM nginx:1.23.3-alpine as production-stage

# 移除nginx容器的default.conf文件
RUN rm /etc/nginx/conf.d/default.conf
# 把主机的nginx.conf文件复制到nginx容器的/etc/nginx文件夹下
COPY ./custom.conf /etc/nginx/conf.d/
# 拷贝前端vue项目打包后生成的文件到nginx下运行
COPY dist /usr/share/nginx/html

# 检查是否存在 index.html 文件
RUN if [ ! -f /usr/share/nginx/html/web/index.html ]; then \
    echo "Error: 请先打包前端，或者使用Dockerfile-build 运行:docker build -f Dockerfile-build -t \"plooks-web\" ."; \
    exit 1; \
    fi

# 暴露2024端口
EXPOSE 2024
# 使用daemon off的方式将nginx运行在前台保证镜像不至于退出
CMD ["nginx", "-g", "daemon off;"]