package com.example.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {
    private String fileName;
    private Long length;
    private MediaType mediaType;
    private Resource file;
}
