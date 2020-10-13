package com.semicolondevop.suite.service.activity;

import com.semicolondevop.suite.model.activity.Activity;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.repository.activityRepository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 5:07 AM
 * @project com.semicolondevop.suite.service.activity in ds-suite
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepo;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }


    public Activity save(Activity activity) {
        if (activity.getId() == null) { // new activity (user logged in)
            Activity firstActivity = this.findFirst();
            if (firstActivity != null) {
                long total = firstActivity.getTotalVisitors();
                activity.setTotalVisitors(++total);
                firstActivity.setTotalVisitors(total);
                this.activityRepo.save(firstActivity);
            }
        }
        return this.activityRepo.save(activity);
    }

    @Override
    public Activity findFirst() {
        return activityRepo.findFirstBy();
    }

    @Override
    public Activity findLast(Developer developer) {
        return activityRepo.findFirstByUserOrderByIdDesc(developer);
    }


    // todo : find the solution to new PageRequest class
//    @Override
//    public Page<Activity> findByUser(Developer developer, int page, int size) {
//        return activityRepo.findByUser(developer, new PageRequest(page, size, Direction.DESC,"id"));;
//    }

//    @Override
//    public Activity findOne(Activity id) {
//        return activityRepo.findOne(id);
//    }

    @Override
    public List<Activity> findAll() {
        return this.activityRepo.findAll();
    }

    @Override
    public void delete(Activity id) {
        activityRepo.delete(id);
    }
}
