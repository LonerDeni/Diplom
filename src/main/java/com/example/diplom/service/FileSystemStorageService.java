package com.example.diplom.service;

import com.example.diplom.model.FileResponse;
import com.example.diplom.model.FileUpload;
import com.example.diplom.model.NewFileName;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public interface FileSystemStorageService {
    Path init(Long token);

    String uploadFile(MultipartFile file, String token);

    FileUpload downloadFile(String fileName, String token);

    String editFileName(String fileName, NewFileName newFileName, String token);

    String deleteFile(String fileName, String token);

    List<FileResponse> getAllFiles(Integer limit, String token);
}
