package com.example.ex02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Xử lý CSRF: Vô hiệu hóa hoàn toàn vì đây là REST API Stateless,
                // không sử dụng Cookie để lưu trữ thông tin xác thực.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Vô hiệu hóa Form Login mặc định của Spring (vì API trả về JSON, không render HTML)
                .formLogin(AbstractHttpConfigurer::disable)

                // Tắt HTTP Basic Authentication (Tùy chọn, nhưng nên làm đối với API dùng Token)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 3. Cấu hình Session Management: Đảm bảo Spring Security không tạo ra Session.
                // Điều này ép hệ thống tuân thủ nghiêm ngặt chuẩn Stateless của REST.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Phân quyền truy cập các Endpoints
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập công khai vào các API đăng ký và đăng nhập
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                        // Yêu cầu xác thực cho TẤT CẢ các API còn lại
                        .requestMatchers("/api/**").authenticated()

                        // Bắt mọi request khác (nếu có) cũng phải xác thực để tránh bỏ lọt
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}