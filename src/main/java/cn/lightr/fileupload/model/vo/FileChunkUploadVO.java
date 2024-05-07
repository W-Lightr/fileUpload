package cn.lightr.fileupload.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lightr
 * @date 2024/4/18
 * @Description 文件分片上传返回实体
 */
@ApiModel(value = "文件分片上传返回实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileChunkUploadVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否需要合并文件 0 不需要 1 需要")
    private Integer mergeFlag;
}
