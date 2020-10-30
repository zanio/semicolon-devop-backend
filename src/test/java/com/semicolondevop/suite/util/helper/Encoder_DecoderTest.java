package com.semicolondevop.suite.util.helper;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.codec.binary.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 3:01 AM
 * @project com.semicolondevop.suite.util.helper in ds-suite
 */
@Slf4j
public class Encoder_DecoderTest {




    @Test
    public void it_should_encode_to_base64_string(){
       String str = Encoder_Decoder.encodeStr("semicolon-devops:semicolon-devops");
       log.info("THE ENCODED STRING IS {}", str);

    }

    @Test
    public void it_should_decode_to_base64_string(){
        String str = Encoder_Decoder.encodeStr("");
        log.info("THE ENCODED STRING IS {}", str);
        String strdecode = Encoder_Decoder.decodeStr(str);
        log.info("THE ENCODED STRING IS {}", strdecode);

    }

}