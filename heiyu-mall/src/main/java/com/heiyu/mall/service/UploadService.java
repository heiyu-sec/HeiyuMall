package com.heiyu.mall.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface UploadService {

        String getNewFileName(@RequestParam("file") MultipartFile multipartFile);

        void createFile(@RequestParam("file") MultipartFile file, File fileDirectory, File destFile);


}
