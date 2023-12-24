package com.example.diplom.service;

import com.example.diplom.entity.FileEntity;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.exception.FileException;
import com.example.diplom.model.FileResponse;
import com.example.diplom.model.FileUploadDTO;
import com.example.diplom.model.NewFileName;
import com.example.diplom.repository.FileRepositories;
import com.example.diplom.repository.UserRepositories;
import com.example.diplom.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.http.ResponseEntity.ok;


@Service
@Slf4j
@RequiredArgsConstructor
public class FileSystemStorageServiceImpl implements FileSystemStorageService {

    private final FileRepositories fileRepositories;
    private final UserRepositories userRepositories;
    private final JWTUtil jwtUtil;

    @Override
    public String uploadFile(MultipartFile file, String token) {
        checkSuccessToken(token);
        Long idUser = getUserId(token);
        Path fileDirectory = init(idUser);
        FileEntity fileData = fileRepositories.findByFileName(file.getOriginalFilename());
        if (file.isEmpty()) {
            log.error(String.format("Error upload file: %s file not found" ,file.getOriginalFilename()));
            throw new FileException("File not found");
        }
        if (!isNull(fileData)) {
            log.error(String.format("Error upload file: %s file already exists", file.getOriginalFilename()));
            throw new FileException("File already exists");
        }
        try {
            fileRepositories.save(new FileEntity(file.getOriginalFilename(), idUser, fileDirectory.toString(), file.getSize()));
            Files.copy(file.getInputStream(), fileDirectory.resolve(file.getOriginalFilename()));
            log.info(String.format("Successful upload file: %s",file.getOriginalFilename()));
            return "Success upload";
        } catch (Exception e) {
            log.error(String.format("Error upload file: %s", file.getOriginalFilename()));
            throw new FileException("File upload error");
        }
    }

    @Override
    public String deleteFile(String fileName, String token) {
        checkSuccessToken(token);
        Long userId = getUserId(token);
        FileEntity fileData = fileRepositories.findByUserIdAndFileName(userId, fileName);
        boolean isFileDelete;
        if (isNull(fileData)) {
            log.error("Error delete file: " + fileName + " file not found");
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        fileRepositories.delete(fileData);
        Path pathFileDelete = Paths.get(fileData.getPath());
        try {
            Path file = pathFileDelete.resolve(fileName);
            isFileDelete = Files.deleteIfExists(file);
            log.info(String.format("File with name: %s delete", fileName));
        } catch (IOException e) {
            log.error("Error delete file");
            throw new FileException("Error delete file");
        }
        if (isFileDelete) {
            return "Success delete";
        }
        log.info("File not delete");
        return String.format("File with name: %s NOT delete", fileName);
    }

    @Override
    public FileUploadDTO downloadFile(String fileName, String token) {
        checkSuccessToken(token);
        Long userId = getUserId(token);
        FileEntity fileData = fileRepositories.findByUserIdAndFileName(userId, fileName);
        if (isNull(fileData)) {
            log.info(String.format("Error download file: %s file not found",fileName));
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        try {
            File file = new File(fileData.getPath() + "/" + fileData.getFileName());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            log.info(String.format("File download: %s",fileName));
            FileUploadDTO fileUpload = new FileUploadDTO();
            fileUpload.setFileName(fileData.getFileName());
            fileUpload.setLength(file.length());
            fileUpload.setMediaType(MediaType.parseMediaType(Files.probeContentType(Paths.get(fileData.getPath() + "/" + fileData.getFileName()))));
            fileUpload.setFile(resource);
            return fileUpload;
        } catch (IOException e) {
            log.error(String.format("Error download file: %s", fileName));
            throw new FileException("File download error");
        }
    }

    @Override
    public String editFileName(String fileName, NewFileName newFileName, String token) {
        checkSuccessToken(token);
        Long userId = getUserId(token);
        FileEntity fileData = fileRepositories.findByUserIdAndFileName(userId, fileName);
        if (isNull(fileData)) {
            log.error(String.format("Error renamed file: %s file not found", fileName));
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        FileEntity tableNewNameFiles = fileRepositories.findByFileName(newFileName.getName());
        if (!isNull(tableNewNameFiles)) {
            log.error(String.format("Error renamed file: %s already exists", newFileName.getName()));
            throw new FileException(String.format("File with name: %s already exists", newFileName.getName()));
        }
        fileData.setFileName(newFileName.getName());
        fileRepositories.save(fileData);
        try {
            Path pathFileDelete = Paths.get(fileData.getPath()).resolve(fileName);
            Files.move(pathFileDelete, pathFileDelete.resolveSibling(newFileName.getName()));
            log.info(String.format("File %s renamed. New name: %s", fileName, newFileName));
            return String.format("File renamed. New name: %s", newFileName.getName());
        } catch (IOException e) {
            log.error(String.format("Error renamed file: %s", fileName));
            throw new FileException("File renamed error");
        }
    }

    @Override
    public List<FileResponse> getAllFiles(Integer limit, String token) {
        checkSuccessToken(token);
        Long userId = getUserId(token);
        List<FileEntity> fileListUser = fileRepositories.findByUserId(userId);
        log.info("List files search");
        return fileListUser.stream().map(x -> new FileResponse(x.getFileName(), x.getSize())).limit(limit).collect(Collectors.toList());
    }

    @Override
    public Path init(Long idUser) {
        String rootLocation = "src/main/resources/files";
        Path pathLocation = Paths.get(rootLocation + "/" + idUser);
        if (!Files.exists(pathLocation)) {
            try {
                Files.createDirectories(pathLocation);
                log.info("Directories for user create");
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize folder for upload!");
            }
        }
        return pathLocation;
    }

    private Long getUserId(String token){
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        return userEntity.get().getId();
    }

    public void checkSuccessToken(String authToken) {
        boolean authSuccess = jwtUtil.validateJwtToken(authToken);
        if (!authSuccess) {
            new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}