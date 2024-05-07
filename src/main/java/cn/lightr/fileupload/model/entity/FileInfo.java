package cn.lightr.fileupload.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 物理文件信息表
 * </p>
 *
 * @author Lightr
 * @since 2024-04-18
 */
@Getter
@Setter
@TableName("file_info")
@ApiModel(value = "FileInfo对象", description = "物理文件信息表")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    @TableId("file_id")
    private String fileId;

    @ApiModelProperty("转存文件名称")
    private String fileTransferName;

    @ApiModelProperty("文件物理路径")
    private String realPath;

    @ApiModelProperty("文件实际大小")
    private String fileSize;

    @ApiModelProperty("文件大小展示字符")
    private String fileSizeDesc;

    @ApiModelProperty("文件源名称")
    private String fileSourceName;

    @ApiModelProperty("文件预览的响应头Content-Type的值")
    private String filePreviewContentType;

    @ApiModelProperty("文件唯一标识")
    private String identifier;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("删除标志")
    private int deleteFlag;

    public static final String FILE_ID = "file_id";

    public static final String FILE_TRANSFER_NAME = "file_transfer_name";

    public static final String REAL_PATH = "real_path";

    public static final String FILE_SIZE = "file_size";

    public static final String FILE_SIZE_DESC = "file_size_desc";

    public static final String FILE_SOURCE_NAME = "file_source_name";

    public static final String FILE_PREVIEW_CONTENT_TYPE = "file_preview_content_type";

    public static final String IDENTIFIER = "identifier";

    public static final String CREATE_USER_ID = "create_user_id";

    public static final String CREATE_USER_NAME = "create_user_name";

    public static final String CREATE_TIME = "create_time";

    public static final String DELETE_FLAG = "delete_flag";
}
