package com.semicolondevop.suite.model.repository.dao.post;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:36 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepoResponsePush {
    private Content content;
    private Commit commit;

}
