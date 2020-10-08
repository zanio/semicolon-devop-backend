package com.semicolondevop.suite.model.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:20 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private String name;
    private Date date;
}
