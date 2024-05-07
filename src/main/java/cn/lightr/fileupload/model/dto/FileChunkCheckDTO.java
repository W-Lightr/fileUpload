package cn.lightr.fileupload.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "文件分片上传查询校验")
@Data
public class FileChunkCheckDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件对应的唯一标识", required = true)
    @NotBlank(message = "文件对应的唯一标识不能为空")
    private String identifier;




}
