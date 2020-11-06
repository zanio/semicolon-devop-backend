package com.semicolondevop.suite.util.task;

import com.semicolondevop.suite.model.activity.Activity;
import com.semicolondevop.suite.service.activity.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 6:00 AM
 * @project com.semicolondevop.suite.util.task in ds-suite
 */

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private ActivityService activityService;


    @Scheduled(cron = "0 1 15 * * ?")
    void deleteActivitiesOlderThanAMonth() {
        Activity firstActivity = this.activityService.findFirst();
        List<Activity> activityList = this.activityService.findAll();
        for (Activity activity : activityList) {
            if (!activity.getId().equals(firstActivity.getId())) { // exclude first activity
                if (daysBetween(activity.getCreated(), new Date()) >= 31) {
                    this.activityService.delete(activity);
                    log.info("Deleted activity! {} ", activity.getId());
                }
            }
        }
        log.info("Deleted old user activities!");
    }

    private float daysBetween(Date dateBefore, Date dateAfter) {
        long difference = dateAfter.getTime() - dateBefore.getTime();
        return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
    }

}
