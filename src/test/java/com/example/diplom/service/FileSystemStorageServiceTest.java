package com.example.diplom.service;

import com.example.diplom.entity.FileEntity;
import com.example.diplom.entity.UserEntity;
import com.example.diplom.repository.FileRepositories;
import com.example.diplom.repository.UserRepositories;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.example.diplom.service.PrepareInfoForTest.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
public class FileSystemStorageServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepositories fileRepositories;
    @Autowired
    private UserRepositories userRepositories;

    @AfterEach
    public void cleanTestUserData() {
        fileRepositories.deleteAll();
        try {
            FileUtils.cleanDirectory(new File(PATHTESTFILE));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @BeforeEach
    public void addFileForTest(){
        userRepositories.save(new UserEntity("testUnitData","5dfc91e02ecc53592738b6c36eedb875","Ivan","Petrov","testUserData@test.com"));
        try{
            File f = new File(PATHTESTFILE + "/testFile.txt");
            if (f.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        } catch (Exception e) {
            e.getMessage();
        }
        Long id = userRepositories.findByLogin("testUnitData").get().getId();
        fileRepositories.save(new FileEntity("testFile.txt", id, PATHTESTFILE, 15L));
    }



    @Test
    public void uploadValidFileTest() throws Exception {
        mockMvc.perform(multipart("/file")
                        .file(getFileToUploadTest())
                        .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success upload"));
        FileEntity file = fileRepositories.findByFileName(FILENAME);
        assertNotNull(file);
    }

    @Test
    public void uploadFileAlreadyExistsTest() throws Exception {
        mockMvc.perform(multipart("/file")
                        .file(getFileToUploadTest())
                        .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"File already exists\"}"));
    }

    @Test
    public void uploadFileEmptyTest() throws Exception {
        mockMvc.perform(multipart("/file")
                        .file("files", null)
                        .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"File not found\"}"));
        ;
    }

    @Test
    public void deleteValidFileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/file")
                        .param("filename", "testFile.txt")
                        .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success delete"));
        ;
        FileEntity file = fileRepositories.findByFileName(FILENAME);
        assertNull(file);
    }

    @Test
    public void deleteFileNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cloud/file")
                        .param("filename", "test.xml")
                        .header("auth-token", createTestToken()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"File with name: test.xml not found\"}"));
    }

    @Test
    public void uploadFileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/file")
                        .param("filename", "testFile.txt")
                        .header("auth-token", createTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    public void uploadFileNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/file")
                        .param("filename", "test.xml")
                        .header("auth-token", createTestToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":null,\"message\":\"File with name: test.xml not found\"}"));
        ;
    }

    @Test
    public void getListFileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .param("limit", "10")
                        .header("auth-token", createTestToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}