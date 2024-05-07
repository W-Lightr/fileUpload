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

![image-20240507135641416](E:\Code\study\fileUpload\assets\image-20240507135641416.png)

![image-20240507135755092](E:\Code\study\fileUpload\assets\image-20240507135755092.png)

![image-20240507140225621](E:\Code\study\fileUpload\assets\image-20240507140225621.png)

![image-20240507140242988](E:\Code\study\fileUpload\assets\image-20240507140242988.png)