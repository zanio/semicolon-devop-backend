package com.semicolondevop.suite.config;


import com.semicolondevop.suite.model.login.LoginDto;
import com.fasterxml.classmate.TypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.*;

/* Aniefiok
 *created on 5/27/2020
 *inside the package */

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
@Slf4j
public class SwaggerLoginListingScanner implements ApiListingScannerPlugin {

    // tag::api-listing-plugin[]
    private final CachingOperationNameGenerator operationNames;

    /**
     * @param operationNames - CachingOperationNameGenerator is a component bean
     *                       that is available to be autowired
     */
    public SwaggerLoginListingScanner(CachingOperationNameGenerator operationNames) {//<9>
        this.operationNames = operationNames;
    }

    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        return new ArrayList<>(
                Arrays.asList(
                        new ApiDescription(null, "/api/auth/login", "login", Collections.singletonList(
                                new OperationBuilder(operationNames)
                                        .summary("login")
                                        .tags(Set.of("Login Services"))
                                        .authorizations(new ArrayList<>())
                                        .position(1)
                                        .codegenMethodNameStem("loginPost")
                                        .method(HttpMethod.POST)
                                        .notes("This is a login method")
                                        .parameters(
                                                Arrays.asList(
                                                        new ParameterBuilder()
                                                                .description("Login Parameter")
                                                                .type(new TypeResolver().resolve(LoginDto.class))
                                                                .name("userLogin")
                                                                .parameterType("body")
                                                                .parameterAccess("access")
                                                                .required(true)
                                                                .modelRef(new ModelRef("LoginDTO"))
                                                                .build()
                                                )
                                        ).responseMessages(responseMessages())
                                        .responseModel(new ModelRef(("LoginDTO")))
                                        .build()
                        ), false)));
    }

    /**
     * @return Set of response messages that overide the default/global response messages
     */
    private Set<ResponseMessage> responseMessages() { //<8>
        return Set.of(new ResponseMessageBuilder()
                        .code(200)
                        .responseModel(new ModelRef("LoginDTO"))
                        .build(), new ResponseMessageBuilder()
                        .code(401)
                        .responseModel(new ModelRef("LoginDTO"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(403)
                        .responseModel(new ModelRef("LoginDTO"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(404)
                        .responseModel(new ModelRef("LoginDTO"))
                        .build()
        );
    }
    // tag::api-listing-plugin[]

    @Override
    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }
}
