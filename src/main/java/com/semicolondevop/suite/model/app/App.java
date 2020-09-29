package com.semicolondevop.suite.model.app;

/* Aniefiok
 *created on 5/27/2020
 *inside the package */

import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.env.Env;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull
    @Size(max = 255)
    private String domain;

    @NotNull
    @Size(max = 255)
    private String title;

    @ManyToOne
    private Developer developer;

    @OneToMany
    private Set<Env> envSet;

    public App(@NotNull App app) {
        this.domain = app.getDomain();
        this.title = app.getTitle();
    }

    public void addEnv(Env env){
        if (envSet == null) {
            envSet = new HashSet<>();
        }
        envSet.add(env);
    }

}
