package com.semicolondevop.suite.model.developer;

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
    private String firstname = "";
    private String lastname ="";
    private String phoneNUmber;
    private String authId;

    public String getUserFirstname(){
       return this.getName().split(" ")[0];
    }
    public String getUserLastname(){
        return this.getName().split(" ")[1];
    }
}

