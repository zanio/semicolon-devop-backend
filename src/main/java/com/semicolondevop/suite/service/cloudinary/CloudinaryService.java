package com.semicolondevop.suite.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/*
 *@author Aniefiok Akpan
 */
@Service
@Slf4j
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinaryConfig;

    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinaryConfig.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return  uploadResult.get("url").toString();
        } catch (Exception e) {
            log.info(" error => {} ",e);   
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File fileToBeUploaded = new File("upload",file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(fileToBeUploaded);
        fileToBeUploaded.delete();
        fos.write(file.getBytes());
        fos.close();
        return fileToBeUploaded;
    }
}
