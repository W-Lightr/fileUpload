package cn.lightr.fileupload.model.dto;

import lombok.Data;

/**
 * @author Lightr
 * @date 2024/10/29
 * @Description 删除
 */
@Data
public class DeleteDto {
    private String fileId;
    private String realPath;
    private boolean deleteFile;
}
