package cn.lightr.fileupload.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lightr
 * @date 2024/4/18
 * @Description 响应单个字符串用
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseString {
    private Object result;
    private Object data;
}
