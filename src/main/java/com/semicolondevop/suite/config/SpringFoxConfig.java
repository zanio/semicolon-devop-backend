package com.semicolondevop.suite.config;

/* Aniefiok
 *created on 5/23/2020
 *inside the package */

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig  extends WebSecurityConfigurerAdapter {

    public static String[] SWAGGER_URL_PATHS = new String[] { "/swagger-ui.html**", "/swagger-resources/**",
            "/v2/api-docs**", "/webjars/**" };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
            .antMatchers(SWAGGER_URL_PATHS)
                .and().authorizeRequests().antMatchers(SWAGGER_URL_PATHS)
                .permitAll();
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class).select()
                .apis(RequestHandlerSelectors.basePackage("com.alaajo.projectalaajo.client"))
                .paths(PathSelectors.any()).build()

                .securitySchemes(newArrayList(apiKey()))
                .securityContexts(newArrayList(securityContext()))
                .apiInfo(getApiInfo())
                .tags(new Tag("Admin Services","All Api related to admin services"),
                        new Tag("Investment Services","All Api related to investment services"),
                        new Tag("Password Services","All Api related to Password services"),
                        new Tag("Admin Statistics Services","All Api related to Admin Statistics services"),
                        new Tag("Developer Services","All Api related to Developer services"),
                        new Tag("User Investment Services","All Api related to User Investment services")
                        );

    }


    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "DS-SUITE",
                "DESCRIPTION",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("Semicolon-devops-suite", "https://semicolon-ds-staging.herokuapp.com/", "EMAIL"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList());
    }

    @Primary
    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(paths())
                .build();
    }

    private Predicate<String> paths() {
        return or(
                regex("/api/admin.*"),
                regex("/api/investment.*"),
                regex("/api/savers/all"),
                regex("/api/savers.*")
        );
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }

    @Bean
    SecurityConfiguration securities() {
        return SecurityConfigurationBuilder.builder()
                .clientId("test-app-client-id")
                .clientSecret("test-app-client-secret")
                .realm("test-app-realm")
                .appName("test-app")
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    @Bean
    UiConfiguration uiConfigs() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

}
