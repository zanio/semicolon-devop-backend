package com.semicolondevop.suite.dao.webhook;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 31/10/2020 - 7:56 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GithubWebhookPayload {
    private String name;
    private Boolean active;
    private List<String> events;

    private GithubWebhookConfiguration config;

    public void add(String nameOfEventsToAdd){
        if(events == null){
            events = new ArrayList<>();
        }
        events.add(nameOfEventsToAdd);
    }
    public void remove(String nameOfEventsToRemove){
        if(events.size()>0){
            events.remove(nameOfEventsToRemove);
        }
    }
}
