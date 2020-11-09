package com.semicolondevop.suite.model.developer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 05/10/2020 - 6:52 PM
 * @project com.semicolondevop.suite.model.developer in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
public class GithubDeveloperDao {
    private String login;
    private String avatar_url;
    private String name;

    private String password;
    @ApiModelProperty(hidden = true)
    private String firstname = "";
    @ApiModelProperty(hidden = true)
    private String lastname ="";

    private String phoneNumber;
    private String authId;

    @ApiModelProperty(hidden = true)
    public String getUserFirstname(){
       return this.getName().split(" ")[0];
    }
    @ApiModelProperty(hidden = true)
    public String getUserLastname(){
        return this.getName().split(" ")[1];
    }
}

