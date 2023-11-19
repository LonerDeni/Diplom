package com.example.diplom.service;

import com.example.diplom.entity.FileEntity;
import com.example.diplom.model.FileResponse;
import com.example.diplom.model.NewFileName;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public interface FileSystemStorageService {
    Path init(Long token);

    ResponseEntity<String> uploadFile(MultipartFile file, String token);
    ResponseEntity<Object> downloadFile(String fileName,String token);
    ResponseEntity<String> editFileName(String fileName, NewFileName newFileName,String token);
    ResponseEntity<String> deleteFile(String fileName, String token);
    ResponseEntity<List<FileResponse>> getAllFiles(Integer limit, String token);
}
