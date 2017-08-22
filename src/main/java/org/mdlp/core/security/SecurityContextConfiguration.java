package org.mdlp.core.security;

import org.jetbrains.annotations.NotNull;
import org.mdlp.utils.AppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@ConditionalOnClass({EnableWebSecurity.class, WebSecurityConfigurerAdapter.class})
@Import(ArmConfiguration.class)
public class SecurityContextConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers("/login.html")
                .antMatchers("/images/**")
                .antMatchers("/js/**")
                .antMatchers("/fonts/**")
                .antMatchers("/css/**")
                .antMatchers("/ws")
                .antMatchers("/rest/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                anyRequest().authenticated().
                and().
                logout().deleteCookies("token").invalidateHttpSession(true).
                and().
                formLogin().loginPage("/login.html");

        http.addFilterBefore(authentificationFilter(), BasicAuthenticationFilter.class);
    }

    @NotNull
    @Bean
    public AuthenticationFilter authentificationFilter() throws Exception {
        return new AuthenticationFilter(authenticationManager());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(inMemoryUsernamePasswordAuthenticationProvider()).
                authenticationProvider(tokenAuthenticationProvider());
    }


    @Bean
    public TokenService tokenService() {
        return new TokenService();
    }

    @Bean
    public ArmConfiguration armConfiguration() {
        return new ArmConfiguration();
    }

    @Bean
    @Autowired
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        armConfiguration().init();
        for (org.mdlp.core.security.user.User user : AppContext.getInstance().getUsers()) {
            manager.createUser(User.withUsername(user.getLogin()).password(user.getPassword()).roles(user.getRole()).build());
        }
        return manager;
    }

    @Bean
    public AuthenticationProvider inMemoryUsernamePasswordAuthenticationProvider() {
        return new MemoryUsernameAutheticationProvider(tokenService(), userDetailsService());
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(tokenService());
    }
}
