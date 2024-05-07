package cn.lightr.fileupload.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "文件秒传")
public class FileSecUploadDTO implements Serializable {

    private static final long serialVersionUID = 1883102378277804464L;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;

    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private String parentId;

}
