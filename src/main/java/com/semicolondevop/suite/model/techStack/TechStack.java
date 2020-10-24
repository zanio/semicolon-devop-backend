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
@NoArgsConstructor
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

    public TechStack(String techStackValue) {
        this.typeOfApplication = TypeOfApplication.DECOUPLED;
        switch(techStackValue) {
            case "JAVA":
                this.techStackType = TechStackType.JAVA;
                break;
            case "PYTHON":
                this.techStackType = TechStackType.PYTHON;
                break;
            case "NODE":
                this.techStackType = TechStackType.NODE;
                break;

            case "REACT":
                this.techStackType = TechStackType.REACT;
                break;

            case "VUE":
                this.techStackType = TechStackType.VUE;
                break;
        }
    }
}
