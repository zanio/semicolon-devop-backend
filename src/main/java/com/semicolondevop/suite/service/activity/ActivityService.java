package com.semicolondevop.suite.service.activity;

import com.semicolondevop.suite.model.activity.Activity;
import com.semicolondevop.suite.model.developer.Developer;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 5:05 AM
 * @project com.semicolondevop.suite.service.activity in ds-suite
 */
public interface ActivityService {
    Activity findFirst();
    Activity findLast(Developer developer);
//    Page<Activity> findByUser(Developer developer, int page, int size);
//    Activity findOne(long id);
    List<Activity> findAll();
    void delete(Activity activity);
    Activity save(Activity activity);
}
