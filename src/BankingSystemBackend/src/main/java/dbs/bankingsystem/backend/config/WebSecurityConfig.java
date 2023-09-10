package dbs.bankingsystem.backend.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import dbs.bankingsystem.backend.security.auth.myUserDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /*@Qualifier("userDetailService")
    @Autowired
    private UserDetailsService jwtUserDetailsService;*/

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private myUserDetailsService userDetailsService;

   /* @Autowired
    private JwtRequestFilter jwtRequestFilter;*/


    @Autowired
    private DataSource dataSource;

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {*/

            http
                .csrf().disable().authorizeRequests()
                //.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers( "/api/user").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers(HttpMethod.GET, "/roleHierarchy").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/showReg", "/",  "/registerUser", "/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/css/**","/lib/**","/images/**","/js/**", "/login/**", "/reservation/**").permitAll()
                .antMatchers("/reserve/showCompleteReservation").authenticated()
                    .antMatchers( "/flights/showFlights","/flights/addFlights", "/flights/findFlights").authenticated()
                .antMatchers(HttpMethod.GET,"/flights/admin/showAddFlight","/flights/admin/addFlight","/flights/admin/*").hasAnyAuthority("ADMIN").anyRequest().hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login").usernameParameter("email").permitAll()
                .successHandler(customAuthenticationSuccessHandler())
                .defaultSuccessUrl("/flights/addFlights", true)
                .failureHandler(authenticationFailureHandler())
                .failureUrl("/userLogin?error=loginError")
                .and().logout().logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .logout().permitAll()
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/invalidSession.html")
                .maximumSessions(2)
                .expiredUrl("/sessionExpired.html");

               //return http.build();

                /*http.cors();
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);*/


    }




    @PostConstruct
    public void completeSetup() {
       userDetailsService = applicationContext.getBean(myUserDetailsService.class);
    }

    @Bean
    public UserDetailsManager users(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .authenticationProvider(authenticationProvider())
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setAuthenticationManager(authenticationManager);
        return jdbcUserDetailsManager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }


    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new customisedAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}
