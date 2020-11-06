package com.semicolondevop.suite.repository.activityRepository;

import com.semicolondevop.suite.model.activity.Activity;
import com.semicolondevop.suite.model.developer.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 5:02 AM
 * @project com.semicolondevop.suite.repository.activityRepository in ds-suite
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Activity findFirstBy();
    Activity findFirstByDeveloperOrderByIdDesc(Developer developer);
    Page<Activity> findByDeveloper(Developer developer, Pageable pageable);
}

