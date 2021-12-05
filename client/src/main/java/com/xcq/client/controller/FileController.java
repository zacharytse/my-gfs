package com.xcq.client.controller;

import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.xcq.client.config.SystemException;
import com.xcq.client.constant.SysConstant;
import com.xcq.client.constant.UrlConstant;
import com.xcq.client.model.dto.AddFileDto;
import com.xcq.client.model.dto.GetFileDto;
import com.xcq.client.model.entity.Files;
import com.xcq.client.model.response.ErrorCode;
import com.xcq.client.model.response.RestResponse;
import com.xcq.client.model.response.RestResponses;
import com.xcq.client.model.response.Result;
import com.xcq.client.service.IFileService;
import com.xcq.client.utils.EmptyUtils;
import com.xcq.client.utils.EncodingUtils;
import com.xcq.client.utils.InputStreamUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>
 * 文件上传下载 前端控制器
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
@Slf4j
@RestController
@RequestMapping(UrlConstant.API + "/file")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    /**
     * 文件列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    public RestResponse<List<GetFileDto>> getFileList(@RequestParam Integer pageNo, @RequestParam Integer pageSize) throws IOException {
        return RestResponses.newResponseFromResult(fileService.getFileList(pageNo, pageSize));
    }

    /**
     * 普通上传方式上传文件：用于小文件的上传，等待时间短，不会产生配置数据
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public RestResponse<String> uploadFiles(MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponses.newFailResponse(ErrorCode.INVALID_PARAMETER, "文件不能为空");
        }
        return RestResponses.newResponseFromResult(fileService.uploadFiles(file));
    }

    /**
     * 添加文件
     * 断点续传完成后上传文件信息进行入库操作
     *
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public RestResponse<String> addFile(@RequestBody AddFileDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RestResponses.newFailResponse(ErrorCode.INVALID_PARAMETER, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return RestResponses.newResponseFromResult(fileService.addFile(dto));
    }

    /**
     * 检查文件MD5（文件MD5若已存在进行秒传）
     *
     * @param md5
     * @param fileName
     * @return
     */
    @GetMapping(value = "/check-file")
    public RestResponse<Object> checkFileMd5(String md5, String fileName) {
        return RestResponses.newResponseFromResult(fileService.checkFileMd5(md5, fileName));
    }

    /**
     * 断点续传方式上传文件：用于大文件上传
     *
     * @param param
     * @param request
     * @return
     */
    @PostMapping(value = "/breakpoint-upload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;charset=UTF-8")
    public RestResponse<Object> breakpointResumeUpload(UploadFileParam param, HttpServletRequest request) {
        return RestResponses.newResponseFromResult(fileService.breakpointResumeUpload(param, request));
    }

    /**
     * 图片/PDF查看
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/view/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> viewFilesImage(@PathVariable String id) throws IOException {
        Result<Files> fileDetails = fileService.getFileDetails(id);
        if (fileDetails.isSuccess()) {
            if (!EmptyUtils.basicIsEmpty(fileDetails.getData().getSuffix()) && !SysConstant.IMAGE_TYPE.contains(fileDetails.getData().getSuffix())) {
                throw new SystemException(ErrorCode.FILE_ERROR.getCode(), "非图片/PDF类型请先下载");
            }
        } else {
            throw new SystemException(fileDetails.getErrorCode().getCode(), fileDetails.getDescription());
        }
        InputStream inputStream = fileService.getFileInputStream(id);
        return new ResponseEntity<>(InputStreamUtils.inputStreamToByte(inputStream), HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param id
     * @param isSource
     * @param request
     * @param response
     */
    @GetMapping(value = "/download/{id}")
    public void viewFilesImage(@PathVariable String id, @RequestParam(required = false) Boolean isSource, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            Result<Files> fileDetails = fileService.getFileDetails(id);
            if (!fileDetails.isSuccess()) {
                throw new SystemException(fileDetails.getErrorCode().getCode(), fileDetails.getDescription());
            }
            String filename = (!EmptyUtils.basicIsEmpty(isSource) && isSource) ? fileDetails.getData().getFileName() : fileDetails.getData().getFilePath();
            inputStream = fileService.getFileInputStream(id);
            response.setHeader("Content-Disposition", "attachment;filename=" + EncodingUtils.convertToFileName(request, filename));
            // 获取输出流
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("文件下载出错", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

