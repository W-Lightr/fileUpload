package cn.lightr.fileupload.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 检查文件分片返回实体
 */
@ApiModel(value = "检查文件分片返回实体")
@Data
public class CheckFileChunkUploadVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 已上传的分片下标集合
     */
    @ApiModelProperty("已上传的分片下标集合")
    private List<Integer> uploadedChunks;



}
