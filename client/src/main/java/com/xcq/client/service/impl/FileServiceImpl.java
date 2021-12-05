package com.xcq.client.service.impl;

import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.xcq.client.constant.SysConstant;
import com.xcq.client.mapper.FilesMapper;
import com.xcq.client.model.dto.AddFileDto;
import com.xcq.client.model.dto.GetFileDto;
import com.xcq.client.model.entity.Files;
import com.xcq.client.model.response.ErrorCode;
import com.xcq.client.model.response.Result;
import com.xcq.client.model.response.Results;
import com.xcq.client.service.IFileService;
import com.xcq.client.utils.EmptyUtils;
import com.xcq.client.utils.NovelWebUtils;
import com.xcq.client.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 文件上传下载 服务实现类
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

    @Value("${file.save-path:/data-center/files/vip-file-manager}")
    private String savePath;
    @Value("${file.conf-path:/data-center/files/vip-file-manager/conf}")
    private String confFilePath;

    @Override
    public Result<List<GetFileDto>> getFileList(Integer pageNo, Integer pageSize) {
        try {
            //PageHelper.startPage(pageNo, pageSize);
//            List<GetFileDto> result = filesMapper.selectFileList();
//            PageInfo<GetFileDto> pageInfo = new PageInfo<>(result);
//            return Results.newSuccessResult(pageInfo.getList(), "查询成功", pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取文件列表出错", e);
        }
        return Results.newFailResult(ErrorCode.DB_ERROR, "查询失败");
    }

    @Override
    public Result<String> uploadFiles(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            // 文件名非空校验
            if (EmptyUtils.basicIsEmpty(fileName)) {
                return Results.newFailResult(ErrorCode.FILE_ERROR, "文件名不能为空");
            }
            // 大文件判定
            if (file.getSize() > SysConstant.MAX_SIZE) {
                return Results.newFailResult(ErrorCode.FILE_ERROR, "文件过大，请使用大文件传输");
            }
            // 生成新文件名
            String suffixName = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : null;
            String newName = UuidUtils.uuid() + suffixName;
            // 重命名文件
            File newFile = new File(savePath, newName);
            // 如果该存储路径不存在则新建存储路径
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            // 文件写入
            file.transferTo(newFile);
            // 保存文件信息
            Files files = new Files().setFilePath(newName).setFileName(fileName).setSuffix(suffixName == null ? null : suffixName.substring(1));
            //filesMapper.insert(files);
            return Results.newSuccessResult(files.getId(), "上传完成");
        } catch (Exception e) {
            log.error("上传协议文件出错", e);
        }
        return Results.newFailResult(ErrorCode.FILE_ERROR, "上传失败");
    }

    @Override
    public Result<Object> checkFileMd5(String md5, String fileName) {
        try {
            cn.novelweb.tool.http.Result result = LocalUpload.checkFileMd5(md5, fileName, confFilePath, savePath);
            return NovelWebUtils.forReturn(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Results.newFailResult(ErrorCode.FILE_UPLOAD, "上传失败");
    }

    @Override
    public Result breakpointResumeUpload(UploadFileParam param, HttpServletRequest request) {
        try {
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(param, confFilePath, savePath, 5242880L, request);
            return NovelWebUtils.forReturn(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Results.newFailResult(ErrorCode.FILE_UPLOAD, "上传失败");
    }


    @Override
    public Result<String> addFile(AddFileDto dto) {
        try {
            Files file = new Files();
            BeanUtils.copyProperties(dto, file);
//            if (filesMapper.fileIsExist(dto.getFileName())) {
//                return Results.newSuccessResult(null, "添加成功");
//            } else if (filesMapper.insert(file.setFilePath(dto.getFileName())) == 1) {
//                return Results.newSuccessResult(null, "添加成功");
//            }
        } catch (Exception e) {
            log.error("添加文件出错", e);
        }
        return Results.newFailResult(ErrorCode.FILE_UPLOAD, "添加失败");
    }

    @Override
    public InputStream getFileInputStream(String id) {
        try {
            //Files files = filesMapper.selectById(id);
            // TODO
            Files files = null;
            File file = new File(savePath + File.separator + files.getFilePath());
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("获取文件输入流出错", e);
        }
        return null;
    }

    @Override
    public Result<Files> getFileDetails(String id) {
        try {
            Files files = null;
            return Results.newSuccessResult(files, "查询成功");
        } catch (Exception e) {
            log.error("获取文件详情出错", e);
        }
        return Results.newFailResult(ErrorCode.DB_ERROR, "查询失败");
    }
}
