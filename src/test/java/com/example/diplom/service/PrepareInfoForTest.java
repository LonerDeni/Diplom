package com.example.diplom.service;

import com.example.diplom.entity.FileEntity;
import com.example.diplom.repository.FileRepositories;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class PrepareInfoForTest {

   // private FileRepositories fileRepositories;
    public final static String FILENAME = "test.txt";

    public final static String PATHTESTFILE = "/Users/deniskachalov/IdeaProjects/Netology/Diplom/src/main/resources/files";
//   public PrepareInfoForTest(FileRepositories fileRepositories){
//       this.fileRepositories = fileRepositories;
//   }
    public static MockMultipartFile getFileToUploadTest() {
        return new MockMultipartFile("files",
                FILENAME, MediaType.TEXT_PLAIN_VALUE, "Test, add file!".getBytes());
    }

//    public void uploadFileTestData() {
//        try{
//            File f = new File(PATHTESTFILE + "/testFile.txt");
//            if (f.createNewFile())
//                System.out.println("File created");
//            else
//                System.out.println("File already exists");
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        fileRepositories.save(new FileEntity("testFile.txt", 1L, PATHTESTFILE, 15L));
//    }

    public static String createTestToken(){
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        String tokenUser = Jwts.builder().setSubject("testUnitData").setIssuedAt(new Date())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, "qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w==").compact();
       return tokenUser;
    }

}
