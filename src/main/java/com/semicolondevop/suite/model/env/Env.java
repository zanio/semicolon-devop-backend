package com.semicolondevop.suite.model.env;

/* Aniefiok
 *created on 09/29/2020
 *inside the package */

import com.semicolondevop.suite.model.app.App;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;
import java.util.Set;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Env {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull
    @Size(max = 225)
    private String key;

    @NotNull
    @Size(max = 225)
    private String value;

    @CreationTimestamp
    private Date dateCreated;

    @ManyToOne
    private App app;

}
