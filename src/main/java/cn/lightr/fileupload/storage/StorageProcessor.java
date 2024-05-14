package cn.lightr.fileupload.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 描述
 */
public interface StorageProcessor {
    /**
     * 文件存储
     *
     * @param inputStream 文件输入流
     * @param totalSize   文件大小
     * @param suffix      文件名后缀
     * @return 文件存储路径
     * @throws IOException
     */
    String store(InputStream inputStream, Long totalSize, String suffix) throws IOException;

    /**
     * 文件分片存储
     *
     * @param inputStream 文件输入流
     * @param identifier  文件唯一标识
     * @param totalChunks 分片总数
     * @param chunkNumber 当前分片下标 从1开始
     * @param totalSize   文件总大小
     * @param chunkSize   分片文件大小
     * @param suffix      文件名后缀
     * @param userId      用户ID
     * @return 文件存储路径
     * @throws IOException
     */
    String storeWithChunk(InputStream inputStream, String identifier, Integer totalChunks, Integer chunkNumber, Long totalSize, Long chunkSize, String suffix) throws IOException;

    /**
     * 读取文件进输出流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    void read2OutputStream(String filePath, OutputStream outputStream) throws IOException;

    /**
     * 删除文件
     *
     * @param cacheKey 缓存key 可以为null
     * @param filePath 文件路径
     * @throws IOException
     */
    void delete(String cacheKey, String filePath) throws IOException;

    /**
     * 分片合并
     *
     * @param cacheKey   缓存key
     * @param attachment 附加信息
     * @return
     */
    String mergeChunks(String cacheKey, Object attachment,String identifier) throws IOException;

    /**
     * 获取文件对象
     * @param identifier md5
     * @param realPath realPath
     * @return
     * @throws IOException
     */
    InputStream download(String identifier,String realPath) throws IOException;
}
