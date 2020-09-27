package com.semicolondevop.suite.service.taskrunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/* Aniefiok
 *created on 5/19/2020
 *inside the package */

@Slf4j
public class ApplicationStartupRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Your application started with option names : {}", args.getOptionNames());
    }
}
