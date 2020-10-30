package com.semicolondevop.suite.model.crumb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.semicolondevop.suite.model.developer.Developer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 4:29 PM
 * @project com.semicolondevop.suite.model.crumb in ds-suite
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Crumb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String crumbRequestField;

    private String crumb;

    private String _class;

    private Date createdDate;

    @JsonProperty("X-Jenkins-Session")
    private String crumbSession;

    @OneToOne
    @JsonBackReference
    private Developer developer;

//    @MAn


}
