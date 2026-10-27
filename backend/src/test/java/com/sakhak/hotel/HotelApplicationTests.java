package com.sakhak.hotel;

import com.sakhak.hotel.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HotelApplicationTests {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void contextLoads() {
    }

    @Test
    void jwtFilterIsConfigured() {
        boolean hasJwtFilter = securityFilterChain.getFilters()
                .stream()
                .anyMatch(filter -> filter instanceof JwtAuthenticationFilter);

        assertTrue(hasJwtFilter, "JWT authentication filter should be attached to the security chain");
    }

}
