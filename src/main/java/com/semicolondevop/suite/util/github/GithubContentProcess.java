package com.semicolondevop.suite.util.github;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 5:23 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
 public  class GithubContentProcess {
    public static String encoder(File file) {
        String base64File = "";
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.encodeBase64String(fileData);
        } catch (FileNotFoundException e) {
            log.info("FILE NOT FOUND {}" + e.getLocalizedMessage());
        } catch (IOException ioe) {
            log.info("EXCEPTION WHILE READING THE FILE {}" + ioe.getLocalizedMessage());
        }

        return base64File;
    }

    public static String decoder(String string) throws IOException {
        byte[] decodedArrayByte = Base64.decodeBase64(string);
        return new String(decodedArrayByte);

    }
}
