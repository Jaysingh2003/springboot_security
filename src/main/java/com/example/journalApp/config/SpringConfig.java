package com.example.journalApp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringConfig {


    @Autowired
    private UserDetailsService  userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;


 @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(customizer->customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("users/register","users/login")
                         .permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // ✅ Fix: Use hasAuthority
                        .anyRequest().authenticated())
//               .formLogin(Customizer.withDefaults())//with the help pof this the login form will disabled so can we can recieve the token
                  .httpBasic(Customizer.withDefaults()) //with the help of this we can se the login form not the  whole code
                 .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


         return http.build();
    }


    //here we give the userdetails to the springsecirty which was implemented by the MYUserDetailsService.
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    //implementiatiom JWT
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
     return config. getAuthenticationManager();
    }
}
