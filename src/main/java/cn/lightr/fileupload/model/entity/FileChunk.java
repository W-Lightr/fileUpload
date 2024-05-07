package cn.lightr.fileupload.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文件分片信息表
 * </p>
 *
 * @author Lightr
 * @since 2024-04-22
 */
@Getter
@Setter
@TableName("file_chunk")
@ApiModel(value = "FileChunk对象", description = "文件分片信息表")
public class FileChunk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("文件唯一标识")
    private String identifier;

    @ApiModelProperty("分片真实的存储路径")
    private String realPath;

    @ApiModelProperty("分片编号")
    private Integer chunkNumber;

    @ApiModelProperty("过期时间")
    private LocalDateTime expirationTime;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public static final String ID = "id";

    public static final String IDENTIFIER = "identifier";

    public static final String REAL_PATH = "real_path";

    public static final String CHUNK_NUMBER = "chunk_number";

    public static final String EXPIRATION_TIME = "expiration_time";

    public static final String CREATE_USER = "create_user";

    public static final String CREATE_TIME = "create_time";
}
