package com.example.diplom.controller;

import com.example.diplom.model.FileResponse;
import com.example.diplom.model.NewFileName;
import com.example.diplom.service.FileSystemStorageService;
import com.example.diplom.service.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class FileController {
    private final FileSystemStorageService fileSystemStorageService;
    private JWTUtil jwtUtil;

    @Autowired
    public FileController(FileSystemStorageService fileSystemStorageService, JWTUtil jwtUtil) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/cloud/file")
    public ResponseEntity<String> uploadFile(@RequestHeader("auth-token") String authToken, @RequestParam("files") MultipartFile file) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return fileSystemStorageService.uploadFile(file, authToken);
    }

    @DeleteMapping("/cloud/file")
    public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return fileSystemStorageService.deleteFile(fileName, authToken);
    }

    @GetMapping("/cloud/file")
    public ResponseEntity<Object> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return fileSystemStorageService.downloadFile(fileName, authToken);
    }

    @PutMapping("/cloud/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, @RequestBody NewFileName newFileName) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return fileSystemStorageService.editFileName(fileName, newFileName, authToken);
    }

    @GetMapping("/cloud/list")
    public ResponseEntity<List<FileResponse>> getListFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return fileSystemStorageService.getAllFiles(limit, authToken);
    }
}