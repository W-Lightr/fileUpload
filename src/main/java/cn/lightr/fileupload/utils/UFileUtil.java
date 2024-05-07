package cn.lightr.fileupload.utils;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 文件工具类
 */
public class UFileUtil {
    private static final String KB_STR = "K";
    private static final String MB_STR = "M";
    private static final String GB_STR = "G";
    private static final String POINT_STR = ".";
    private static final Long ZERO_LONG = 0L;
    private static final Integer ZERO_INT = 0;
    private static final String EMPTY_STR = "";
    private static final Integer MINUS_ONE_INT = -1;
    public static final String SEPARATOR = "-BLOCK-";
    private static final Integer UNIT = 1024;
    private static final String FILE_SIZE_DESC_FORMAT = "%.2f";
    /**
     * 通过文件名获取文件后缀
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        if (StringUtils.isBlank(filename) || (filename.indexOf(POINT_STR) == MINUS_ONE_INT)) {
            return EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(POINT_STR)).toLowerCase();
    }

    /**
     * 将输入流中的数据写入指定的文件。
     *
     * @param inputStream 输入流，提供要写入文件的数据。
     * @param targetFile 目标文件，指定数据写入的文件路径。
     * @param totalSize 要写入的数据总大小，单位为字节。
     * @throws IOException 如果在读取输入流、写入文件或关闭资源时发生I/O错误。
     */
    public static void writeStreamToFile(InputStream inputStream, File targetFile, Long totalSize) throws IOException {
        // 确保目标文件的父目录存在，如果不存在则创建父目录
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        // 创建目标文件
        targetFile.createNewFile();
        // 使用随机访问文件来操作文件
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        // 获取随机访问文件的文件通道
        FileChannel fileChannel = randomAccessFile.getChannel();
        // 从输入流创建一个可读的字节通道
        ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        // 将输入流中的数据传输到目标文件中，从文件开头开始传输，传输totalSize字
        fileChannel.transferFrom(readableByteChannel, ZERO_LONG, totalSize);
        // 关闭所有相关的资源
        fileChannel.close();
        randomAccessFile.close();
        readableByteChannel.close();
    }

    /**
     * 获取输入流写入输出流
     *
     * @param fileInputStream
     * @param outputStream
     * @param size
     * @throws IOException
     */
    public static void writeFileToStream(FileInputStream fileInputStream, OutputStream outputStream, Long size) throws IOException {
        FileChannel fileChannel = null;
        WritableByteChannel writableByteChannel = null;
        try {
            fileChannel = fileInputStream.getChannel();
            writableByteChannel = Channels.newChannel(outputStream);
            fileChannel.transferTo(ZERO_LONG, size, writableByteChannel);
            outputStream.flush();
        } catch (Exception e) {
        } finally {
            fileInputStream.close();
            outputStream.close();
            fileChannel.close();
            writableByteChannel.close();
        }
    }

    /**
     * 生成文件本地的保存路径
     *
     * @param filePrefix 路径
     * @param suffix 文件拓展名
     * @return path
     */
    public static String generateFilePath(String filePrefix, String suffix) {
        return new StringBuffer(filePrefix)
                .append(File.separator)
                .append(IdUtil.simpleUUID())
                .append(suffix)
                .toString();
    }

    /**
     * 根据分片文件名称解析分片文件的分片号
     *
     * @param chunkFilename 文件名称
     * @return
     */
    public static Integer resolveChunkFileNumber(String chunkFilename) {
        if (StringUtils.isNotBlank(chunkFilename)) {
            String chunkNumberStr = chunkFilename.substring(chunkFilename.lastIndexOf(SEPARATOR) + SEPARATOR.length());
            return Integer.valueOf(chunkNumberStr);
        }
        return ZERO_INT;
    }
    /**
     * 追加写文件
     *
     * @param target 目标文件
     * @param source 源文件
     * @throws IOException
     */
    public static void appendWrite(Path target, Path source) throws IOException {
        Files.write(target, Files.readAllBytes(source), StandardOpenOption.APPEND);
    }

    /**
     * 删除物理文件
     *
     * @param filePath 文件物理路径
     */
    public static void delete(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        delete0(new File(filePath));
    }
    /**
     * 递归删除文件
     *
     * @param file
     * @throws IOException
     */
    private static void delete0(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    delete0(files[i]);
                }
            }
            Files.delete(file.toPath());
        } else {
            Files.delete(file.toPath());
        }
    }

    /**
     * 获取文件大小字符串
     *
     * @param size
     * @return
     */
    public static String getFileSizeDesc(long size) {
        double fileSize = (double) size;
        String fileSizeSuffix = KB_STR;
        fileSize = fileSize / UNIT;
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = MB_STR;
        }
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = GB_STR;
        }
        return String.format(FILE_SIZE_DESC_FORMAT, fileSize) + fileSizeSuffix;
    }

    /**
     * 获取文件的content-type
     *
     * @param filePath
     * @return
     */
    public static String getContentType(String filePath) {
        //利用nio提供的类判断文件ContentType
        File file = new File(filePath);
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //若失败则调用另一个方法进行判断
        if (StringUtils.isBlank(contentType)) {
            contentType = new MimetypesFileTypeMap().getContentType(file);
        }
        return contentType;
    }



    public static void addCorsResponseHeader(HttpServletResponse response) {
        response.setHeader(CorsConfigEnum.CORS_ORIGIN.getKey(), CorsConfigEnum.CORS_ORIGIN.getValue());
        response.setHeader(CorsConfigEnum.CORS_CREDENTIALS.getKey(), CorsConfigEnum.CORS_CREDENTIALS.getValue());
        response.setHeader(CorsConfigEnum.CORS_METHODS.getKey(), CorsConfigEnum.CORS_METHODS.getValue());
        response.setHeader(CorsConfigEnum.CORS_MAX_AGE.getKey(), CorsConfigEnum.CORS_MAX_AGE.getValue());
        response.setHeader(CorsConfigEnum.CORS_HEADERS.getKey(), CorsConfigEnum.CORS_HEADERS.getValue());
    }
    /**
     * 跨域设置枚举类
     */
    public enum CorsConfigEnum {
        /**
         * 允许所有远程访问
         */
        CORS_ORIGIN("Access-Control-Allow-Origin", "*"),
        /**
         * 允许认证
         */
        CORS_CREDENTIALS("Access-Control-Allow-Credentials", "true"),
        /**
         * 允许远程调用的请求类型
         */
        CORS_METHODS("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"),
        /**
         * 指定本次预检请求的有效期，单位是秒
         */
        CORS_MAX_AGE("Access-Control-Max-Age", "3600"),
        /**
         * 允许所有请求头
         */
        CORS_HEADERS("Access-Control-Allow-Headers", "*");

        CorsConfigEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
