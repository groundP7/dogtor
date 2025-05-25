package com.ground.dogtor.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 암호화 처리 클래스 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // 공개 경로
                        .anyRequest().permitAll()
//                                .authenticated() // 나머지는 로그인 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")          // 커스텀 로그인 페이지
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
