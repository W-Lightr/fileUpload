package cn.lightr.fileupload.controller;

import cn.lightr.fileupload.model.dto.FileChunkCheckDTO;
import cn.lightr.fileupload.model.dto.FileChunkMergeDTO;
import cn.lightr.fileupload.model.dto.FileSecUploadDTO;
import cn.lightr.fileupload.model.entity.FileInfo;
import cn.lightr.fileupload.model.file.FileChunkUpload;
import cn.lightr.fileupload.model.vo.CheckFileChunkUploadVO;
import cn.lightr.fileupload.model.vo.FileChunkUploadVO;
import cn.lightr.fileupload.model.vo.ResponseString;
import cn.lightr.fileupload.service.impl.FileInfoServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 物理文件信息表 前端控制器
 * </p>
 *
 * @author Lightr
 * @since 2024-04-18
 */
@RestController
@RequestMapping("/file")
public class FileInfoController {
    @Resource
    private FileInfoServiceImpl fileInfoService;

    /**
     * 查看文件是否已经上传过了
     * @param fileSecUploadPO
     * @return
     */
    @PostMapping("sec-upload")
    public ResponseString secUpload(@Validated @RequestBody FileSecUploadDTO fileSecUploadPO) {
        return new ResponseString(fileInfoService.secUpload(fileSecUploadPO.getIdentifier()));
    }

    /**
     * 分片上传
     * @param fileChunkUpload 文件信息
     * @return 上传结果
     */
    @PostMapping("chunk-upload")
    public FileChunkUploadVO uploadWithChunk(@Validated FileChunkUpload fileChunkUpload) {
        return fileInfoService.uploadWithChunk(fileChunkUpload);
    }
    @GetMapping("chunk-upload")
    public CheckFileChunkUploadVO checkUploadWithChunk(@Validated FileChunkCheckDTO fileChunkCheckDTO) throws IOException {
        return fileInfoService.checkUploadWithChunk(fileChunkCheckDTO.getIdentifier());
    }

    /**
     * 合并文件
     * @param fileChunkMerge 文件信息
     * @return
     */
    @PostMapping("merge")
    public ResponseString mergeChunks(@Validated @RequestBody FileChunkMergeDTO fileChunkMerge) {
        fileInfoService.mergeChunks(fileChunkMerge.getFilename(), fileChunkMerge.getIdentifier(), fileChunkMerge.getTotalSize(),0L);
        return new ResponseString(true);
    }



    //获取文件列表
    @GetMapping("list")
    public List<FileInfo> list() {
        return fileInfoService.list(new QueryWrapper<FileInfo>().eq(FileInfo.DELETE_FLAG, 0));
    }
    //删除文件
    @PostMapping("delete")
    public ResponseString delete( String fileId,String realPath) {
        return new ResponseString(fileInfoService.delete(fileId,realPath));
    }

    @GetMapping("download")
    public void download(String fileId, HttpServletResponse response) throws IOException {
        fileInfoService.download(fileId, response);
    }

}
