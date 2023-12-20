package com.example.diplom.controller;

import com.example.diplom.model.FileResponse;
import com.example.diplom.model.FileUpload;
import com.example.diplom.model.NewFileName;
import com.example.diplom.service.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
        String upload = fileSystemStorageService.uploadFile(file, authToken);
        return ResponseEntity.ok()
                .body(upload);
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        String delete = fileSystemStorageService.deleteFile(fileName, authToken);
        return ResponseEntity.ok().body(delete);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName) {
        FileUpload fileUpload = fileSystemStorageService.downloadFile(fileName, authToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, fileUpload.getHeader())
                .contentLength(fileUpload.getLength())
                .contentType(fileUpload.getType())
                .body(fileUpload.getFile());
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String fileName, @RequestBody NewFileName newFileName) {
        String edit = fileSystemStorageService.editFileName(fileName, newFileName, authToken);
        return ResponseEntity.ok().body(edit);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> getListFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit) {
        List<FileResponse> getFiles = fileSystemStorageService.getAllFiles(limit, authToken);
        return ResponseEntity.ok().body(getFiles);
    }
}