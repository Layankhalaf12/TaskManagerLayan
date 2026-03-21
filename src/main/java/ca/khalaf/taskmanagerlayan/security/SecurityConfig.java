package ca.khalaf.taskmanagerlayan.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // USER 1 -
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("user1"))
                .roles("USER")
                .build();

        // USER 2 -
        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("user2"))
                .roles("USER")
                .build();

        // ADMIN
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(Authorize -> {
            Authorize
                    .requestMatchers("/css/**").permitAll()        // static css
                    .requestMatchers("/").permitAll()              // public home page
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // user area
                    .requestMatchers("/admin/**").hasRole("ADMIN") // admin area only
                    .anyRequest().authenticated();                 // everything else needs login
        });

        http.formLogin(Customizer.withDefaults()); // Spring Security default login page

        http.logout(logout -> logout
                .logoutSuccessUrl("/")  // after logout go to home
                .permitAll()
        );

        http.exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")  // custom 403 page
        );

        return http.build();
    }
}
