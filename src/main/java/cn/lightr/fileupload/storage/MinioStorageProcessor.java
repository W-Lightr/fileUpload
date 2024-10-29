package cn.lightr.fileupload.storage;

import cn.hutool.core.util.IdUtil;
import cn.lightr.fileupload.utils.MinioUtil;
import cn.lightr.fileupload.utils.UFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 描述
 */
@Slf4j
@Component
public class MinioStorageProcessor implements StorageProcessor {

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public String store(InputStream inputStream, Long totalSize, String suffix) throws IOException {
        return "";
    }

    @Override
    public String storeWithChunk(InputStream inputStream, String identifier, Integer totalChunks, Integer chunkNumber, Long totalSize, Long chunkSize, String suffix) throws IOException {
        // 构造分片上传路径
        String chunkFilePath = new StringBuffer()
                .append(IdUtil.simpleUUID()+UFileUtil.SEPARATOR+chunkNumber).toString();
        //上传到minio
        //先确定桶是否存在，没有则创建
        if (!minioUtil.bucketExists(identifier)){
            minioUtil.createBucket(identifier);
        }
        String upload = minioUtil.upload(identifier, inputStream, chunkSize, chunkFilePath);
        log.debug("【storeWithChunk】:当前分片文件路径:{}",chunkFilePath);
        return upload;
    }

    @Override
    public void read2OutputStream(String filePath, OutputStream outputStream) throws IOException {

    }

    @Override
    public void delete(String cacheKey, String filePath) throws IOException {
        minioUtil.delete(minioUtil.bucketName, filePath);
    }

    /**
     *
     * @param cacheKey   缓存key 这里直接使用filename截取文件后缀
     * @param attachment 附加信息
     * @return
     * @throws IOException
     */
    @Override
    public String mergeChunks(String cacheKey, Object attachment,String identifier) throws IOException {
        //UUID文件名称
        String fileName = IdUtil.simpleUUID()+UFileUtil.getFileSuffix(cacheKey);
        //合并
        minioUtil.merge(identifier, minioUtil.bucketName, fileName);
        //删除合并后的桶
        minioUtil.removeBucket(identifier);
        return fileName;
    }

    @Override
    public InputStream download(String identifier, String realPath) throws IOException {
        return minioUtil.getObject(realPath);
    }
}
