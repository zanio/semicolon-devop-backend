package com.semicolondevop.suite.util.helper;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import org.apache.commons.codec.binary.Base64;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 3:01 AM
 * @project com.semicolondevop.suite.util.helper in ds-suite
 */
public final class Encoder_Decoder {

    public static String encodeStr(String str){
        byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
       return new String(bytesEncoded);
    }
    public static String decodeStr(String str){
        byte[] bytesDecoded = Base64.decodeBase64(str.getBytes());
        return new String(bytesDecoded);
    }
}
