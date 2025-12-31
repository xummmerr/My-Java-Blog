package com.xummmerr.blog.controller;


import com.xummmerr.common.Result; // 你的统一返回结果类
import cn.hutool.core.io.FileUtil; // 建议引入 Hutool 工具包，或者用原生写法
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    // 我们可以把文件存在项目根目录下的 files 文件夹里
    // System.getProperty("user.dir") 获取当前项目路径
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        if (file == null) {
            return Result.error("未上传文件");
        }

        // 1. 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 2. 生成一个唯一的文件名（防止覆盖），比如：uuid.jpg
        String mainName = IdUtil.simpleUUID();
        String extName = FileUtil.extName(originalFilename);
        String projectFileName = mainName + "." + extName;

        // 3. 确保存储目录存在
        File parentFile = new File(ROOT_PATH);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 4. 保存文件到硬盘
        File saveFile = new File(ROOT_PATH + projectFileName);
        file.transferTo(saveFile);

        // 5. 返回文件的访问链接
        // 假设你的端口是 8080，我们把 /files/ 作为静态资源映射路径
        String url = "http://localhost:8080/files/" + projectFileName;

        return Result.success(url);
    }
}
