package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer // torna a classe como um servidor de recursos do OAUTH2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment environment; //Ambiente de toda a aplicação Spring
    @Autowired
    private JwtTokenStore tokenStore;

    private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"};

    private static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"};

    private static final String[] ADMIN = {"/users/**"};

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore); //com isso o resource server sera capaz
        // de decodificar o token e verificar se ele bate com o secret
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        if(Arrays.asList(environment.getActiveProfiles()).contains("local")){
            /*
            A interface do H2 requer que desabilite os frames
             */
            http.headers().frameOptions().disable();
        }

        http.authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
                .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
                .antMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
