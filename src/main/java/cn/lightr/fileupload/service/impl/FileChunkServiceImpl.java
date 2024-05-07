package cn.lightr.fileupload.service.impl;

import cn.lightr.fileupload.model.entity.FileChunk;
import cn.lightr.fileupload.mapper.FileChunkMapper;
import cn.lightr.fileupload.service.intf.IFileChunkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件分片信息表 服务实现类
 * </p>
 *
 * @author Lightr
 * @since 2024-04-22
 */
@Service
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk> implements IFileChunkService {

}
