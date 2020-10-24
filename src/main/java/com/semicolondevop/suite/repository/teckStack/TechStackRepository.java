package com.semicolondevop.suite.repository.teckStack;

import com.semicolondevop.suite.model.techStack.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 24/10/2020 - 11:01 PM
 * @project com.semicolondevop.suite.repository.teckStack in ds-suite
 */
@Repository
public interface TechStackRepository extends JpaRepository<TechStack, Integer> {

}
