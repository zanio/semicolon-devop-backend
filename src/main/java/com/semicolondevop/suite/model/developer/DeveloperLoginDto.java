package com.semicolondevop.suite.model.developer;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 06/10/2020 - 5:07 AM
 * @project com.semicolondevop.suite.model.developer in ds-suite
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class DeveloperLoginDto {
    private String username;
    private String password;
}
