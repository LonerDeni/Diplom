package com.example.diplom.controller;

import com.example.diplom.model.FileResponse;
import com.example.diplom.model.NewFileName;
import com.example.diplom.service.FileSystemStorageService;
import com.example.diplom.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileSystemStorageService fileSystemStorageService;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestHeader("auth-token") String authToken, @RequestParam("files") MultipartFile file) {
        return fileSystemStorageService.uploadFile(file, authToken);
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        return fileSystemStorageService.deleteFile(fileName, authToken);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        return fileSystemStorageService.downloadFile(fileName, authToken);
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, @RequestBody NewFileName newFileName) {
        return fileSystemStorageService.editFileName(fileName, newFileName, authToken);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> getListFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit) {
        return fileSystemStorageService.getAllFiles(limit, authToken);
    }
}