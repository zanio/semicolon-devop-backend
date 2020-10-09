package com.semicolondevop.suite.model.repository;

import lombok.*;

import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:22 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Committer {
    private String name;
    private String email;
    private Date date;
}
