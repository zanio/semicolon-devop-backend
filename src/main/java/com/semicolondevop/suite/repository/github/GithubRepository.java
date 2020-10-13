package com.semicolondevop.suite.repository.github;

import com.semicolondevop.suite.model.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 6:27 PM
 * @project com.semicolondevop.suite.repository.github in ds-suite
 */
@org.springframework.stereotype.Repository
public interface GithubRepository extends JpaRepository<Repository, Integer> {

}
