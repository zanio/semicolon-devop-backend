package com.semicolondevop.suite.model.repository;

import com.semicolondevop.suite.model.app.App;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String nameOfRepo;

    private String pathToConfigurationFile;

    private String repoLink;

    @CreationTimestamp
    private Date dateCreated;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn()
    @ApiModelProperty(hidden = true)
    private App app;


}
