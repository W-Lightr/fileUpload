package cn.lightr.fileupload.utils;

import com.alibaba.fastjson.JSON;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Lightr
 * @date 2024/5/11
 * @Description MinioUtil
 */
@Component
public class MinioUtil {
    private MinioClient minioClient;
    @Value("${minio.bucketName}")
    public String bucketName;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * description: 文件上传
     *
     * @param bucketName 桶名称
     * @param file       文件
     * @param fileName   文件名
     */
    public String upload(String bucketName, MultipartFile file, String fileName) {
        // 返回客户端文件系统中的原始文件名
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .build());
            return bucketName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String upload(String bucketName, InputStream fileInputStream, Long fileSize, String fileName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(fileInputStream, fileSize, -1)
                    .build());
            return bucketName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * description: 文件删除
     */
    public boolean delete(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                    .object(fileName).build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Minio文件删除失败" + e.getMessage());
        }
    }

    /**
     * description: 删除桶
     *
     * @param bucketName 桶名称
     */
    public boolean removeBucket(String bucketName) {
        try {
            List<Object> folderList = getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (!folderList.isEmpty()) {
                for (Object value : folderList) {
                    Map o = (Map) value;
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                for (String fileName : fileNames) {
                    delete(bucketName, fileName);
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Minio删除桶失败:" + e.getMessage());
        }
    }

    /**
     * description: 获取桶下所有文件的文件名+大小
     *
     * @param bucketName 桶名称
     */
    public List<Object> getFolderList(String bucketName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = results.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','fileSize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse((String.format(format, item.objectName(),
                    formatFileSize(item.size())))));
        }
        return items;
    }

    /**
     * description: 格式化文件大小
     *
     * @param fileS 文件的字节长度
     */
    private String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }

    /**
     * 讲快文件合并到新桶   块文件必须满足 名字是 0 1  2 3 5....
     *
     * @param bucketName  存块文件的桶
     * @param bucketName1 存新文件的桶
     * @param fileName1   存到新桶中的文件名称
     * @return boolean
     */
    public boolean merge(String bucketName, String bucketName1, String fileName1) {
        try {
            List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
            List<Object> folderList = getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (!folderList.isEmpty()) {
                for (Object value : folderList) {
                    Map o = (Map) value;
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                fileNames.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {

                        if (Integer.parseInt(o2.split(UFileUtil.SEPARATOR)[1]) > Integer.parseInt(o1.split(UFileUtil.SEPARATOR)[1])) {
                            return -1;
                        }
                        return 1;
                    }
                });
                for (String name : fileNames) {
                    sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(name).build());
                }
            }
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName1)
                            .object(fileName1)
                            .sources(sourceObjectList)
                            .build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Minio合并桶异常" + e.getMessage());
        }
    }

    /**
     * description: 获取桶列表
     */
    public List<String> getBucketList() {
        List<Bucket> buckets = null;
        try {
            buckets = minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException("Minio获取桶列表失败：" + e.getMessage());
        }
        List<String> list = new ArrayList<>();
        for (Bucket bucket : buckets) {
            String name = bucket.name();
            list.add(name);
        }
        return list;
    }

    /**
     * description: 创建桶
     */
    public boolean createBucket(String bucketName) {
        try {
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!b) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Minio创建桶失败：" + e.getMessage());
        }
    }

    /**
     * description: 判断桶是否存在
     */
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new RuntimeException("Minio判断桶是否存在出错：" + e.getMessage());
        }
    }
    // 获取文件对象下载
    public InputStream getObject(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException("Minio获取文件对象出错：" + e.getMessage());
        }
    }
}
