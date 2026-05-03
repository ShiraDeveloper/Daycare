package com.example.daycare.config;

import com.example.daycare.Repository.NannyRepository;
import com.example.daycare.model.Nanny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration

public class SecurityConfig {
    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final NannyRepository nannyRepository;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, NannyRepository nannyRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.nannyRepository = nannyRepository;
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //
                .authorizeRequests()
                //הרשאה לכתובת המכילה pattern מסוים
                .requestMatchers("/api/auth/**")
                .permitAll()
                .requestMatchers("/h2-console/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // .csrf()
                //.ignoringRequestMatchers("/h2-console/**")
                // .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //הגדרת אחראי על אימות המשתמשים בהתאם לתנאי הכניסה שלהם
                .authenticationProvider(authenticationProvider())
                //הוספת פילטר מותאם אישית
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();


    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwodEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwodEncoder() {
        return NoOpPasswordEncoder.getInstance();
        // return  new BCryptPasswordEncoder();
    }

 @Bean
public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            UserDetails user = nannyRepository.findByEmail(email);
            return user;
        }
    };
}

}
