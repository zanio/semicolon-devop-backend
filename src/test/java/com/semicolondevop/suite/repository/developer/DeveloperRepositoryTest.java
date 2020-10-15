package com.semicolondevop.suite.repository.developer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 15/10/2020 - 4:38 AM
 * @project com.semicolondevop.suite.repository.developer in ds-suite
 */

@SpringBootTest
@Sql(scripts={"classpath:/db/development/repository.sql"})
@Slf4j
@Transactional
@ActiveProfiles("test")
class DeveloperRepositoryTest {

}