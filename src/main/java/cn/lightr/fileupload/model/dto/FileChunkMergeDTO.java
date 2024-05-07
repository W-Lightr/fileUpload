package cn.lightr.fileupload.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Lightr
 * @date 2024/4/22
 * @Description 合并文件信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileChunkMergeDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    private Long totalSize;

}
