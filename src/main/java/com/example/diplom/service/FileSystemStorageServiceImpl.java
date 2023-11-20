package com.example.diplom.service;

import com.example.diplom.entity.FileEntity;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.exception.FileException;
import com.example.diplom.model.FileResponse;
import com.example.diplom.model.NewFileName;
import com.example.diplom.repository.FileRepositories;
import com.example.diplom.repository.UserRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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


@Service
@Slf4j
@RequiredArgsConstructor
public class FileSystemStorageServiceImpl implements FileSystemStorageService {

    private final FileRepositories fileRepositories;
    private final UserRepositories userRepositories;
    private final JWTUtil jwtUtil;
    private final Path rootLocation = Paths.get("/Users/deniskachalov/IdeaProjects/Netology/Diplom/src/main/resources/files/");

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, String token) {
        // TODO Этот код как то мб сделать лучше
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        Long idUser = userEntity.get().getId();
        Path fileDirectory = init(idUser);
        // TODO
        FileEntity tableNameFiles = fileRepositories.findByFileName(file.getOriginalFilename());
        if (file.isEmpty()) {
            log.error("Error upload file: " + file.getOriginalFilename() + " file not found");
            throw new FileException("File not found");
        }
        if (!isNull(tableNameFiles)) {
            log.error("Error upload file: " + file.getOriginalFilename() + " file already exists");
            throw new FileException("File already exists");
        }
        try {
            fileRepositories.save(new FileEntity(file.getOriginalFilename(), idUser, fileDirectory.toString(), file.getSize()));
            Files.copy(file.getInputStream(), fileDirectory.resolve(file.getOriginalFilename()));
            log.info("Successful upload file: " + file.getOriginalFilename());
            return new ResponseEntity<>("Success upload", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error upload file: " + file.getOriginalFilename());
            throw new FileException("File upload error");
        }
    }

    @Override
    public ResponseEntity<String> deleteFile(String fileName, String token) {
        // TODO Этот код как то мб сделать лучше
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        Long userId = userEntity.get().getId();
        // TODO
        FileEntity tableNameFiles = fileRepositories.findByUserIdAndFileName(userId, fileName);
        boolean isFileDelete;
        if (isNull(tableNameFiles)) {
            log.error("Error delete file: " + fileName + " file not found");
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        fileRepositories.delete(tableNameFiles);
        Path pathFileDelete = Paths.get(tableNameFiles.getPath());
        try {
            Path file = pathFileDelete.resolve(fileName);
            isFileDelete = Files.deleteIfExists(file);
            log.info(String.format("File with name: %s delete", fileName));
        } catch (IOException e) {
            log.error("Error delete file");
            throw new FileException("Error delete file");
        }
        if (isFileDelete) {
            return new ResponseEntity<>("Success delete", HttpStatus.OK);
        }
        log.info("File not delete");
        return new ResponseEntity<>(String.format("File with name: %s NOT delete", fileName), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> downloadFile(String fileName, String token) {
        // TODO Этот код как то мб сделать лучше
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        Long userId = userEntity.get().getId();
        // TODO
        FileEntity tableNameFiles = fileRepositories.findByUserIdAndFileName(userId, fileName);
        if (isNull(tableNameFiles)) {
            log.error("Error download file: " + fileName + " file not found");
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        try {
            File file = new File(tableNameFiles.getPath() + "/" + tableNameFiles.getFileName());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            log.info("File download: " + fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tableNameFiles.getFileName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get(tableNameFiles.getPath() + "/" + tableNameFiles.getFileName()))))
                    .body(resource);
        } catch (IOException e) {
            log.error("Error download file: " + fileName);
            throw new FileException("File download error");
        }
    }

    @Override
    public ResponseEntity<String> editFileName(String fileName, NewFileName newFileName, String token) {
        // TODO Этот код как то мб сделать лучше
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        Long userId = userEntity.get().getId();
        // TODO
        FileEntity tableNameFiles = fileRepositories.findByUserIdAndFileName(userId, fileName);
        if (isNull(tableNameFiles)) {
            log.error("Error renamed file: " + fileName + " file not found");
            throw new FileException(String.format("File with name: %s not found", fileName));
        }
        FileEntity tableNewNameFiles = fileRepositories.findByFileName(newFileName.getName());
        if (!isNull(tableNewNameFiles)) {
            log.error("Error renamed file: " + newFileName.getName() + " already exists");
            throw new FileException(String.format("File with name: %s already exists", newFileName.getName()));
        }
        tableNameFiles.setFileName(newFileName.getName());
        fileRepositories.save(tableNameFiles);
        try {
            Path pathFileDelete = Paths.get(tableNameFiles.getPath()).resolve(fileName);
            Files.move(pathFileDelete, pathFileDelete.resolveSibling(newFileName.getName()));
            log.info(String.format("File %s renamed. New name: %s", fileName, newFileName));
            return new ResponseEntity<>(String.format("File renamed. New name: %s", newFileName.getName()), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error renamed file: " + fileName);
            throw new FileException("File renamed error");
        }
    }

    @Override
    public ResponseEntity<List<FileResponse>> getAllFiles(Integer limit, String token) {
        // TODO Этот код как то мб сделать лучше
        Optional<UserEntity> userEntity = userRepositories.
                findByLogin(jwtUtil.getUserNameFromJwtToken(token));
        if (!userEntity.isPresent()) {
            throw new FileException("User not found");
        }
        Long userId = userEntity.get().getId();
        // TODO
        List<FileEntity> fileListUser = fileRepositories.findByUserId(userId);
        log.info("List files search");
        return new ResponseEntity<>(fileListUser.stream().map(x -> new FileResponse(x.getFileName(), x.getSize())).limit(limit).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public Path init(Long idUser) {
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

}