package com.semicolondevop.suite.model.logger;

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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull
    @Size(max = 300)
    private String logUrl;

    @ManyToOne
    private App app;

    @CreationTimestamp
    private Date dateCreated;
}
