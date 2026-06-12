package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    /**
     * 本地文件上传
     */
    /*private static final String UPLOAD_DIR = "D:/File/photo/";

    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile file) throws IOException {
        log.info("文件上传{},{},{}",name,age,file);

        if (!file.isEmpty()) {
            //保存文件到本地
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            //生成新的文件名 原理：UUID生成一个唯一的字符串
            UUID uuid = UUID.randomUUID();
            //生成新文件名
            String newFileName = uuid.toString().replace("-","")+extension;

            File dir = new File(UPLOAD_DIR+newFileName);
            //如果父目录不存在则创建目录
            if (dir.getParentFile().exists()) {
                dir.getParentFile().mkdirs();
            }
            // 更改文件存储路径
            file.transferTo(dir);
        }
        return Result.success();
    }*/

    /**
     * 阿里云OOS文件上传
     */
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传{}",file);
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传路径{}",url);
        return Result.success(url);

    }

}
