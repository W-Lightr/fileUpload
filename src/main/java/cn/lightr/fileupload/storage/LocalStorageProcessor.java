package cn.lightr.fileupload.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.lightr.fileupload.config.LocalStorageConfig;
import cn.lightr.fileupload.utils.UFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 描述
 */
@Slf4j
@Component
public class LocalStorageProcessor implements StorageProcessor {

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private LocalStorageConfig localStorageConfig;

    @Override
    public String store(InputStream inputStream, Long totalSize, String suffix) throws IOException {
        return "";
    }

    @Override
    public String storeWithChunk(InputStream inputStream, String identifier, Integer totalChunks, Integer chunkNumber, Long totalSize, Long chunkSize, String suffix) throws IOException {
        // 构造分片上传路径
        String chunkFilePath = new StringBuffer(localStorageConfig.getChunksPath())
                .append(File.separator)
                .append(identifier)
                .append(File.separator)
                .append(UUID.randomUUID(true).toString()+UFileUtil.SEPARATOR+chunkNumber).toString();
        log.debug("【storeWithChunk】:当前分片文件路径:{}",chunkFilePath);
        //将流写入文件
        UFileUtil.writeStreamToFile(inputStream, new File(chunkFilePath), chunkSize);
        return chunkFilePath;
    }

    @Override
    public void read2OutputStream(String filePath, OutputStream outputStream) throws IOException {

    }

    @Override
    public void delete(String cacheKey, String filePath) throws IOException {
        UFileUtil.delete(filePath);
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
        List<String> chunkFilePaths = (List<String>) attachment;
        // 这里cacheKey == filename
        // 构造文件存储路径
        String filePath = UFileUtil.generateFilePath(localStorageConfig.getRootFilePath(), UFileUtil.getFileSuffix(cacheKey));
        //创建文件
        FileUtil.touch(filePath);
        // 遍历分片文件进行排序
        List<File> chunkFiles = chunkFilePaths.stream()
                .map(chunkFilePath -> new File(chunkFilePath))
                .sorted((chunkFile1, chunkFile2) -> {
                    String chunkFile1Name = chunkFile1.getName();
                    String chunkFile2Name = chunkFile2.getName();
                    return UFileUtil.resolveChunkFileNumber(chunkFile1Name).compareTo(UFileUtil.resolveChunkFileNumber(chunkFile2Name));
                })
                .collect(Collectors.toList());
        // 合并文件同时删除合并后的分块文件
        for (File chunkFile : chunkFiles) {
            UFileUtil.appendWrite(Paths.get(filePath), chunkFile.toPath());
            UFileUtil.delete(chunkFile.getAbsolutePath());
        }
        return filePath;
    }

    @Override
    public InputStream download(String identifier, String realPath) throws IOException {
        return Files.newInputStream(new File(realPath).toPath());
    }
}
