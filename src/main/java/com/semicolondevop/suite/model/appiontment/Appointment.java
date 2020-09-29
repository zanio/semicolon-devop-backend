package com.semicolondevop.suite.model.appiontment;

/* Aniefiok
 *created on 09/29/2020
 *inside the package */

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull
    @Size(max = 128)
    private String email;

    private Boolean cleared;

    @NotNull
    @Size(max = 128)
    private String clearedBy;

    @CreationTimestamp
    private Date dateCleared;

}
