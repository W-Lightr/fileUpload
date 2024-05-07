## 文件分片上传

简单大文件分片上传、秒传、续传

技术栈

- SpringBoot
- Mybatis-plus
- Mysql

### 指定文件上传路径

application.yml

```yml
# 指定文件上传路径
storage:
  local:
    root-file-path: /Users/storage
```

### 启动

1. 1.导入ddl.sql，
2. 2.配置数据库连接
3. 3.启动



前端项目:https://github.com/W-Lightr/fileUpload-vue3

![image-20240507135641416](http://mark.lightr.cn/mark/image-20240507135641416.png?%09imageMogr2/auto-orient/blur/1x0/quality/80%7Cwatermark/2/text/QOS5mOmjjuW9kuWOuw==/font/5a6L5L2T/fontsize/260/fill/IzAwMDAwMA==/dissolve/58/gravity/SouthEast/dx/10/dy/10)

![image-20240507135755092](http://mark.lightr.cn/mark/image-20240507135755092.png?%09imageMogr2/auto-orient/blur/1x0/quality/80%7Cwatermark/2/text/QOS5mOmjjuW9kuWOuw==/font/5a6L5L2T/fontsize/260/fill/IzAwMDAwMA==/dissolve/58/gravity/SouthEast/dx/10/dy/10)

![image-20240507140225621](http://mark.lightr.cn/mark/image-20240507140225621.png?%09imageMogr2/auto-orient/blur/1x0/quality/80%7Cwatermark/2/text/QOS5mOmjjuW9kuWOuw==/font/5a6L5L2T/fontsize/260/fill/IzAwMDAwMA==/dissolve/58/gravity/SouthEast/dx/10/dy/10)

![image-20240507140242988](http://mark.lightr.cn/mark/image-20240507140242988.png?%09imageMogr2/auto-orient/blur/1x0/quality/80%7Cwatermark/2/text/QOS5mOmjjuW9kuWOuw==/font/5a6L5L2T/fontsize/260/fill/IzAwMDAwMA==/dissolve/58/gravity/SouthEast/dx/10/dy/10)