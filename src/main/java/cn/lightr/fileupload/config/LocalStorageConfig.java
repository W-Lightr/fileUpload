package cn.lightr.fileupload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 描述
 */
@Component(value = "localStorageConfig")
@ConfigurationProperties(prefix = "storage.local")
@Data
public class LocalStorageConfig {
    /**
     * 实际存放路径前缀
     */
    private String rootFilePath = System.getProperty("user.home") + File.separator + "file";

    /**
     * 临时分片文件存放路径前缀
     */
    private String chunksPath = rootFilePath + File.separator + "chunk_tmp";

}
