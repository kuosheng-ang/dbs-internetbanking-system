# Banking System With Spring Boot And Angular with Spring Security

## Introduction

@author: ANG KUO SHENG CLEMENT

This project is a simplified version that aims to allow users to perform online banking activity. It uses spring boot framework (Java) in the backend & angular in the front-end . Some of the functionalities implemented are
- Account Controller
    - Account creation
    - Retrieve Account Details
    - Perform Transactions to transfer funds between two accounts
    - Fetching of account transaction history
- User Controller
    - Use username & password for sign-in/login authentication
    - IAM policies - role, privileges and permissions tables
    - Support RESTFul api with JWT token generation
      
    ![User-creation-with -JWT-Token](/images/User-creation-with-JWT-Token.PNG "User-creation-with -JWT-Token")

- Authentication Controller
    - Uses Spring Boot Spring Security JWT with settings configured in the WebSecurityConfig including import package from org.springframework.security.oauth2.jwt.* 
      
        -  Maintain the parameter settings in the WebSecurityConfig file by import packages such as:
            import com.nimbusds.jose.JOSEException;
            import com.nimbusds.jose.jwk.JWKSet;
            import com.nimbusds.jose.jwk.RSAKey;
            import com.nimbusds.jose.jwk.source.JWKSource;
            import com.nimbusds.jose.proc.SecurityContext;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.security.authentication.AuthenticationManager;
            import org.springframework.security.authentication.ProviderManager;
            import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
            import org.springframework.security.config.Customizer;
            import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
            import org.springframework.security.config.annotation.web.builders.HttpSecurity;
            import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
            import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
            import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
            import org.springframework.security.config.http.SessionCreationPolicy;
            import org.springframework.security.core.userdetails.User;
            import org.springframework.security.core.userdetails.UserDetailsService;
            import org.springframework.security.oauth2.jwt.JwtDecoder;
            import org.springframework.security.oauth2.jwt.JwtEncoder;
            import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
            import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;     
       -  Maintain in POM.xml file
              <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
             </dependency>    

## Prerequisites
- Java JDK 13
- Oracle 12cR2
- maven 3.x
- npm 10.1.0 for Angular 16.2.4
- node 18.17.1

## ER Diagram

- Banking System ER Entity-Relationship Diagram (ERD) generated from Oracle SQL Developer


![banking system ER diagram](/images/banking-systems-ER-diagram.png "banking-system-ER diagram")

## Unit testing for UserJWTAuthentication Controller


        @WebMvcTest({HomeController.class, AuthController.class})
        @Import({SecurityConfig.class, TokenService.class})
        class UserJWTAuthenticationControllerTest {
        
            @Autowired
            MockMvc mvc;
        
            @Test
            void rootWhenUnauthenticatedThen401() throws Exception {
                this.mvc.perform(get("/"))
                        .andExpect(status().isUnauthorized());
            }
        
            @Test
            void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
                MvcResult result = this.mvc.perform(post("/token")
                                .with(httpBasic("clementkuoshengang", "password")))
                        .andExpect(status().isOk())
                        .andReturn();
        
                String token = result.getResponse().getContentAsString();
        
                this.mvc.perform(get("/")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(content().string("Hello, clementkuoshengang"));
            }
        
            @Test
            @WithMockUser
            public void rootWithMockUserStatusIsOK() throws Exception {
                this.mvc.perform(get("/")).andExpect(status().isOk());
            }
        
        
        }

## Web Security Config


    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {

        private RSAKey rsaKey;
    
        @Bean
        public AuthenticationManager authManager(UserDetailsService userDetailsService) {
            var authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            return new ProviderManager(authProvider);
        }
    
      
    
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests( auth -> auth
                            .requestMatchers("/token").permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                    .build();
        }
    
        @Bean
        public JWKSource<SecurityContext> jwkSource() {
            rsaKey = Jwks.generateRsa();
            JWKSet jwkSet = new JWKSet(rsaKey);
            return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
        }
    
        @Bean
        JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
            return new NimbusJwtEncoder(jwks);
        }
    
        @Bean
        JwtDecoder jwtDecoder() throws JOSEException {
             return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
        }
    
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:5030"));
            configuration.setAllowedMethods(List.of("GET","POST"));
            configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**",configuration);
            return source;
        }

    }

### Steps To Setup Backend

**1. Clone the repository**
```bash
    https://github.com/kuosheng-ang/dbs-internetbanking-system.git![image](https://github.com/kuosheng-ang/dbs-internetbanking-system/assets/90085499/05a7b52c-73ed-4483-965b-5f003011a087)

```

**2. Move to root directory of backend**

**3. Build project**
```bash
    mvn clean install
``` 

**4. Run project** 
```bash
    java -jar target/backend-0.0.1-SNAPSHOT.jar
``` 
- Alternatively, you can run the app without packaging it using -
```bash
    mvn spring-boot:run
```
  #### Explore apis in backend

The app defines following APIs. 
 
```   
    POST /api/signup   
    POST /api/user
    GET /api/user
    PUT /api/user
    GET /api/account
    POST /api/transfer
    GET /api/transaction      
```

### Steps To Setup Frontend

**1. Move To Frontend Derectory**

**2. Install Package**
```bash 
    npm install
```

**3. Run Project**
```bash
    npm start
```

**4. Open url**
```bash
    http://localhost:4200/
```
# Application Screenshots

### Login



### Register



### Home



### Profile



### Transaction History



### Transfer




