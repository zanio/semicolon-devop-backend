package com.semicolondevop.suite.model.app;

/* Aniefiok
 *created on 5/27/2020
 *inside the package */

import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.env.Env;
import com.semicolondevop.suite.model.logger.AppLogger;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.techStack.TechStack;
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

    @NotNull
    @Size(max = 3000)
    private String description;

    @NotNull
    @Size(max = 128)
    private String name;

    @OneToOne
    private TechStack techStack;

    @OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<AppLogger> appLoggers;


    @ManyToOne
    private Developer developer;

    @OneToOne
    private Repository repository;

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

    public boolean removeEnv(Env env){
        boolean isremoved = false;
        if (envSet.size()>0) {
            isremoved = envSet.remove(env);
        }
      return isremoved;
    }

    public void addAppLogger(AppLogger appLogger){
        if(appLoggers == null){
            appLoggers = new HashSet<>();
        }
        appLoggers.add(appLogger);
    }

}
