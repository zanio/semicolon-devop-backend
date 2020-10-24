package com.semicolondevop.suite.security;

/* Aniefiok Akpan
 *created on 5/17/2020
 *inside the package */

import com.semicolondevop.suite.client.exception.securitylayerexeception.RestAccessDeniedHandler;
import com.semicolondevop.suite.client.exception.securitylayerexeception.RestAuthenticationEntryPoint;
import com.semicolondevop.suite.client.exception.securitylayerexeception.RestAuthenticationFailureHandler;
import com.semicolondevop.suite.client.exception.securitylayerexeception.RestAuthenticationSuccessHandler;
import com.semicolondevop.suite.service.developer.UserDetailsServiceImpl;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.semicolondevop.suite.security.SecurityConstansts.*;

@Configuration
@EnableWebSecurity
@Order(1000)
public class SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {


    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher(ADMIN_URL+"/**"),
            new AntPathRequestMatcher(PASSWORD_URL,"POST"),
            new AntPathRequestMatcher(PASSWORD_URL+"/**","PATCH"),
            new AntPathRequestMatcher(PASSWORD_URL+"/**","DELETE")
    );

    @Autowired
    JWTGenericAuthorization jwtGenericAuthorization;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${client.dev.url}")
    private String clientDevUrl;

    @Value("${client.prod.url}")
    private String clientProdUrl;



    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().configurationSource(corsConfigurationSource()).and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
//                .authenticationProvider(provider)
                .addFilterBefore(jwtGenericAuthorization, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationLoginFilter())
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),getApplicationContext()))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(WEB_SOCKET_ENDPOINT,WEB_SOCKET_ENDPOINT_GROUP).permitAll()
                .antMatchers(HttpMethod.POST, PASSWORD_RESET_URL).permitAll()
                .antMatchers(HttpMethod.GET, PASSWORD_CONFIRMATION).permitAll()
                .antMatchers(HttpMethod.POST, PASSWORD_NEW_PASSWORD).permitAll()
                .antMatchers(HttpMethod.POST, ADMIN_URL+"/new").hasAuthority("ROLE_SUPER_ADMIN")
                .requestMatchers(ADMIN_URLS).hasAnyAuthority("ROLE_ADMIN","ROLE_SUPER_ADMIN")
                .antMatchers("/api/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN","ROLE_SUPER_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/developers/confirm").permitAll()
                .antMatchers(HttpMethod.POST, "/alaajo/paystack/event").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
//                .httpBasic().disable()
                .logout().disable();


        http.requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(clientDevUrl,clientProdUrl));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","PUT","DELETE"));
        configuration.setAllowCredentials(true);
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.addAllowedHeader("Authorization");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Access-Control-Allow-Origin");
        configuration.addExposedHeader("Access-Control-Allow-Headers");
        configuration.addExposedHeader("ETag");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    RestAuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    RestAuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    RestAuthenticationSuccessHandler successHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    public JWTAuthenticationFilter jwtAuthorizationLoginFilter() throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager(),getApplicationContext());
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        return jwtAuthenticationFilter;
    }
}
