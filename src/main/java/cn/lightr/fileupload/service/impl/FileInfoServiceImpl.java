package cn.lightr.fileupload.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.lightr.fileupload.constant.enums.FileConstant;
import cn.lightr.fileupload.mapper.FileInfoMapper;
import cn.lightr.fileupload.model.entity.FileChunk;
import cn.lightr.fileupload.model.entity.FileInfo;
import cn.lightr.fileupload.model.file.FileChunkUpload;
import cn.lightr.fileupload.model.vo.CheckFileChunkUploadVO;
import cn.lightr.fileupload.model.vo.FileChunkUploadVO;
import cn.lightr.fileupload.service.intf.IFileInfoService;
import cn.lightr.fileupload.storage.StorageProcessor;
import cn.lightr.fileupload.utils.UFileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 物理文件信息表 服务实现类
 * </p>
 *
 * @author Lightr
 * @since 2024-04-18
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Autowired
    StorageProcessor storageProcessor;
    @Autowired
    FileChunkServiceImpl fileChunkService;
    /**
     * 检查文件秒传
     * @param identifier hash
     * @return boolean
     */
    public FileInfo secUpload(String identifier) {
        //检查数据库是否有该hash值文件
        List<FileInfo> fileInfos = getBaseMapper().selectList(new QueryWrapper<FileInfo>().eq(FileInfo.IDENTIFIER, identifier).eq(FileInfo.DELETE_FLAG, 0));
        if (CollectionUtils.isEmpty(fileInfos)) {
            return null;
        }
        return fileInfos.get(0);
    }

    /**
     * 分片上传
     * @param fileChunkUpload 文件信息
     * @return
     */
    public FileChunkUploadVO uploadWithChunk(FileChunkUpload fileChunkUpload) {
        // 初始化上传对象，合并状态为：0
        FileChunkUploadVO fileChunkUploadVO = new FileChunkUploadVO(FileConstant.MergeFlag.NOT_READY.getCode());
        if (saveWithChunk(fileChunkUpload)) {
            //所有分片上传完成，设置合并状态为1 ,前端发起合并请求
            fileChunkUploadVO.setMergeFlag(FileConstant.MergeFlag.READY.getCode());
        }
        return fileChunkUploadVO;
    }

    /**
     * 检查已上传的分片
     * @param identifier
     * @return 分片number集合
     */
    public CheckFileChunkUploadVO checkUploadWithChunk(String identifier) {
        CheckFileChunkUploadVO checkFileChunkUploadVO = new CheckFileChunkUploadVO();
        List<FileChunk> chunks = fileChunkService.list(new QueryWrapper<FileChunk>().eq(FileChunk.IDENTIFIER, identifier));
        List<Integer> collect = chunks.stream().map(FileChunk::getChunkNumber).collect(Collectors.toList());
        checkFileChunkUploadVO.setUploadedChunks(collect);
        return checkFileChunkUploadVO;
    }


    /**
     * 保存分片
     * @param fileChunkUpload 文件信息
     * @return 是否是最后一个分片
     */
    private synchronized boolean saveWithChunk(FileChunkUpload fileChunkUpload){
        try {
            // 存储分片
            String filePath = storageProcessor.storeWithChunk(fileChunkUpload.getFile().getInputStream(), fileChunkUpload.getIdentifier(), fileChunkUpload.getTotalChunks(), fileChunkUpload.getChunkNumber(), fileChunkUpload.getTotalSize(), fileChunkUpload.getCurrentChunkSize(), UFileUtil.getFileSuffix(fileChunkUpload.getFilename()));
            // 将上传的分片信息保存到数据库，以便合并
            FileChunk chunk = new FileChunk();
            chunk.setChunkNumber(fileChunkUpload.getChunkNumber());
            chunk.setIdentifier(fileChunkUpload.getIdentifier());
            chunk.setRealPath(filePath);
            chunk.setCreateUser("admin");
            chunk.setId(IdUtil.simpleUUID());
            fileChunkService.save(chunk);
            // 查看所有分片是否上传完成
            long count = fileChunkService.count(new QueryWrapper<FileChunk>().eq(FileChunk.IDENTIFIER, fileChunkUpload.getIdentifier()));
            return count == fileChunkUpload.getTotalChunks();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("【saveWithChunk】 上传文件失败！ "+e.getMessage());
        }

    }

    public FileInfo mergeChunks(String filename, String identifier, Long totalSize, String userId) {
        FileInfo fileInfo = new FileInfo();
        //查询分片信息
        List<FileChunk> fileChunks = fileChunkService.list(new QueryWrapper<FileChunk>().eq(FileChunk.IDENTIFIER, identifier));
        if (CollectionUtils.isNotEmpty(fileChunks)){
            //取出文件地址
            List<String> filePaths = fileChunks.stream().map(FileChunk::getRealPath).collect(Collectors.toList());
            // 合并后的文件路径
            String filePath;
            try {
                //合并
                filePath = storageProcessor.mergeChunks(filename, filePaths,identifier);
                //合并完成删除数据库记录的分片信息
                fileChunkService.remove(new QueryWrapper<FileChunk>().eq(FileChunk.IDENTIFIER, identifier));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("合并失败");
            }
            //构建文件信息保存到数据库
            fileInfo.setFileId(IdUtil.simpleUUID()); // 文件主键
            fileInfo.setFileTransferName(FileNameUtil.getName(FileUtil.file(filePath))); // 保存后文件名称
            fileInfo.setRealPath(filePath); // 文件真实存储路径
            fileInfo.setFileSize(totalSize+""); // 文件大小byte
            fileInfo.setFileSizeDesc(UFileUtil.getFileSizeDesc(totalSize)); // 文件大小字符串描述
            fileInfo.setFileSourceName(filename);
            fileInfo.setFilePreviewContentType(UFileUtil.getContentType(filePath));
            fileInfo.setIdentifier(identifier);
            fileInfo.setCreateUserId(userId);
            fileInfo.setCreateUserName("system");
            try {
                save(fileInfo);
            }catch (Exception e){
                // 保存失败删除源文件
                try {
                    storageProcessor.delete(null, filePath);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                throw new RuntimeException("保存文件失败");
            }
        }
        return fileInfo;
    }

    public void download(String fileId, String dxccid, HttpServletResponse response) throws IOException {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        FileInfo files = new FileInfo();
        files.setDeleteFlag(0);
        files.setFileId(fileId);
        files.setRealPath(dxccid);
        wrapper.setEntity(files);
        FileInfo fileInfo = getOne(wrapper);

        if (fileInfo == null){
            throw new RuntimeException("文件不存在");
        }
        response.reset();
        response.setHeader(FileConstant.CONTENT_TYPE_STR, FileConstant.APPLICATION_OCTET_STREAM_STR);
        response.setContentType(FileConstant.APPLICATION_OCTET_STREAM_STR);
        response.setHeader(FileConstant.CONTENT_DISPOSITION_STR, FileConstant.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(fileInfo.getFileSourceName().getBytes(FileConstant.GB2312_STR), FileConstant.IOS_8859_1_STR));
        response.setHeader(FileConstant.CONTENT_LENGTH_STR, fileInfo.getFileSize());
        // 添加跨域头
        UFileUtil.addCorsResponseHeader(response);
        InputStream stream = storageProcessor.download(fileInfo.getIdentifier(), fileInfo.getRealPath());
        UFileUtil.writeStreamToStreamNormal(stream, response.getOutputStream());
    }

    //删除
    public boolean delete(String fileId,String realPath,boolean deleteFile) {
        UpdateWrapper<FileInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(FileInfo.FILE_ID, fileId);
        updateWrapper.eq(FileInfo.REAL_PATH, realPath);
        updateWrapper.set(FileInfo.DELETE_FLAG, 1);
        try {
            if (update(updateWrapper)){
                if (deleteFile){
                    storageProcessor.delete(null, realPath);
                }
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
