package com.semicolondevop.suite.model.repository.dao.get;

import lombok.*;

import java.util.List;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 12:44 PM
 * @project com.semicolondevop.suite.model.repository.dao.get in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListOfRepository {

  private   List<GithubRepoResponseDao> repoResponseDaoList;
}
