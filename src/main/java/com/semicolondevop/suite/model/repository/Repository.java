package com.semicolondevop.suite.model.repository;

import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.techStack.TechStack;
import com.semicolondevop.suite.model.techStack.TechStackType;
import com.semicolondevop.suite.model.techStack.TypeOfApplication;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 4:54 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String fullName;

    @ApiModelProperty(hidden = true)
    private String pathToConfigurationFile;

    private String repoLink;

    @ApiModelProperty(hidden = true)
    @CreationTimestamp
    private Date dateCreated;

    @ApiModelProperty(hidden = true)
    private Boolean isRepoLinkedToJenkins;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn()
    @ApiModelProperty(hidden = true)
    private App app;

    public Repository(Repository repository, App app) {
        this.fullName = repository.getFullName();
        this.repoLink = repository.getRepoLink();
        this.app = app;
        this.isRepoLinkedToJenkins = false;

        innerFieldAttachment(app);
    }
    public Repository(App app) {
        this.app = app;

        innerFieldAttachment(app);
        this.isRepoLinkedToJenkins = false;
    }

    private void innerFieldAttachment(App app) {
        if(app.getTechStack().getTypeOfApplication() == TypeOfApplication.DECOUPLED){
            TechStackType techStackType = app.getTechStack().getTechStackType();
            if(techStackType == TechStackType.JAVA){
                this.pathToConfigurationFile = RepoLinkConstant.JAVA;
            } else if(techStackType == TechStackType.NODE){
                this.pathToConfigurationFile = RepoLinkConstant.NODE;
            } else if(techStackType == TechStackType.PYTHON){
                this.pathToConfigurationFile = RepoLinkConstant.PYTHON;
            } else if(techStackType == TechStackType.VUE){
                this.pathToConfigurationFile = RepoLinkConstant.VUE;
            } else if(techStackType == TechStackType.REACT){
                this.pathToConfigurationFile = RepoLinkConstant.REACT;
            };

        }
    }
}
