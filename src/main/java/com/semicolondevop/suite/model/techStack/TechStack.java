package com.semicolondevop.suite.model.techStack;


import com.semicolondevop.suite.model.app.App;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @NotNull(message = "techStackType field cannot be null")
    @Size(max = 255)
    private TechStackType techStackType;

    @NotNull
    @Size(max = 255)
    private TypeOfApplication typeOfApplication;

    @OneToOne
    @JoinColumn()
    private App app;

    public TechStack() {
        this.typeOfApplication = TypeOfApplication.DECOUPLED;
    }
}
