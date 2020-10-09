package com.semicolondevop.suite.model.repository.dao.get;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 12:30 PM
 * @project com.semicolondevop.suite.model.repository.dao.get in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Owner {
    private String login;
    private String repos_url;
    private String organizations_url;
    private String type;

}
