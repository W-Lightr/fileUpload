package cn.lightr.fileupload.model.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "文件分片上传")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileChunkUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "总分片数", required = true)
    @NotNull(message = "总分片数不能为空")
    private Integer totalChunks;

    @ApiModelProperty(value = "分片下标，从1开始", required = true)
    @NotNull(message = "分片下标不能为空")
    private Integer chunkNumber;

    @ApiModelProperty(value = "当前分片大小", required = true)
    @NotNull(message = "当前分片大小不能为空")
    private Long currentChunkSize;

    @ApiModelProperty(value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    private Long totalSize;

    @ApiModelProperty(value = "上传文件", required = true)
    // @NotNull(message = "上传文件不能为空")
    private MultipartFile file;
}
