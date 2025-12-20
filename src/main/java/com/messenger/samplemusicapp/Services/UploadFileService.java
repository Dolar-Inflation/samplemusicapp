package com.messenger.samplemusicapp.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {


    public String uploadFile( MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        }
        String uploadDir = "uploads/";
        Path path = Paths.get(uploadDir +file.getOriginalFilename());
        Files.write(path,file.getBytes());
        return "/songs/"+file.getOriginalFilename();
    }
    //нужно сделать один метод а то получается какой то очевиднейший говнокод...
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        }
        String uploadDir = "image/";
        Path path = Paths.get(uploadDir +file.getOriginalFilename());
        Files.write(path,file.getBytes());
        return "/albums/"+file.getOriginalFilename();
    }

}
