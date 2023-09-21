//package com.sparta.springcafeservice.config;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Component
//public class UploadFolderInitializer implements ApplicationRunner {
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // "uploads" 폴더의 경로
//        String uploadFolderPath = "static/uploads";
//
//        // 폴더가 없는 경우 생성
//        Path uploadFolder = Paths.get(uploadFolderPath);
//        if (!Files.exists(uploadFolder)) {
//            Files.createDirectories(uploadFolder);
//        }
//    }
//}