package com.semicolondevop.suite.repository.jenkinsCredential;

import com.semicolondevop.suite.model.jenkins.JenkinsCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 7:26 PM
 * @project com.semicolondevop.suite.repository.jenkinsCredential in ds-suite
 */
@Repository
public interface JenkinsCredentialRepository extends JpaRepository<JenkinsCredentials, Integer> {
}
