# 使用官方 Nginx 镜像作为基础
FROM nginx:alpine

# 删除默认配置文件
RUN rm -rf /usr/share/nginx/html/*

# 复制 dist 中的内容到 nginx 网站目录
COPY dist/ /usr/share/nginx/html/

# 复制自定义 nginx 配置（可选）
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
