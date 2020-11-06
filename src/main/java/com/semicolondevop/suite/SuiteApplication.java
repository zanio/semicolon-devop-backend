package com.semicolondevop.suite;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cloudinary.Cloudinary;
import com.semicolondevop.suite.util.helper.Encoder_Decoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.semicolondevop.suite.service.taskrunner.CommandLineRunner;

import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@SpringBootApplication
public class SuiteApplication {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    private  static String  myfile = "my file is \"go\"";

    @Value("${jenkins.auth}")
    private String jenkinsCredentials;

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    public static void main(String[] args) {
        SpringApplication.run(SuiteApplication.class, args);
    }




    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    @Bean
    public JenkinsClient client() {
        String encodedStr = Encoder_Decoder.encodeStr(jenkinsCredentials);
        return JenkinsClient.builder()
                .endPoint(jenkinsUrl) // Optional. Defaults to http://127.0.0.1:8080
                .credentials(encodedStr) // Optional.
                .build();
    }


    @Bean
    public CommandLineRunner run(){
        return new CommandLineRunner();
    }

}
