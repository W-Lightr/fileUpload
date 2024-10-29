# 文件分片上传

![visitors](https://visitor-badge.laobi.icu/badge?page_id=W-Lightr/fileUpload) ![Static Badge](https://img.shields.io/badge/SpringBoot-2.7.6-brightgreen) ![Static Badge](https://img.shields.io/badge/MybatisPlus-3.5.4.1-brightgreen)

简单大文件分片上传、秒传、续传

支持:`Minio`、`LocalStorage`

### 指定文件上传路径

`application.yml`

```yml
# 指定文件上传路径
storage:
  mode: local  # 存储方式 minio local
  local:
    root-file-path: /Users/storage
```

### 启动

1. 1.导入ddl.sql，
2. 2.配置数据库连接
3. 3.启动



### 前端组件

推荐搭配前端组件：https://www.npmjs.com/package/vue-upload-utils



