package ru.yandex.practicum.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String API_PRODUCTS = "/api/products";
    private static final String API_CATEGORIES = "/api/categories";
    private static final String API_INVENTORY = "/api/inventory";
    private static final String API_ORDERS = "/api/orders";
    private static final String API_DOCS = "/api-docs";
    private static final String SWAGGER_UI = "/swagger-ui";
    private static final String SWAGGER_UI_HTML = SWAGGER_UI + ".html";

    private static final String[] PUBLIC_ENDPOINTS = {
            API_PRODUCTS,
            API_PRODUCTS + "/category/{categoryId}",
            API_PRODUCTS + "/search",
            API_PRODUCTS + "/{id}",
            API_PRODUCTS + API_DOCS,
            API_PRODUCTS + SWAGGER_UI,
            API_PRODUCTS + SWAGGER_UI_HTML,
            API_CATEGORIES,
            API_CATEGORIES + "/{id}",
            API_CATEGORIES + API_DOCS,
            API_CATEGORIES + SWAGGER_UI,
            API_CATEGORIES + SWAGGER_UI_HTML,
            API_INVENTORY,
            API_INVENTORY + "/{productId}",
            API_INVENTORY + API_DOCS,
            API_INVENTORY + SWAGGER_UI,
            API_INVENTORY + SWAGGER_UI_HTML,
            API_ORDERS + API_DOCS,
            API_ORDERS + SWAGGER_UI,
            API_ORDERS + SWAGGER_UI_HTML,
    };

    private static final String[] USER_GET_ENDPOINTS = {
            API_ORDERS + "/by-email",
            API_ORDERS + "/{id}"
    };

    private static final String[] USER_POST_ENDPOINTS = {API_ORDERS};

    private static final String[] ADMIN_GET_ENDPOINTS = {API_ORDERS};

    private static final String[] ADMIN_POST_ENDPOINTS = {
            API_PRODUCTS,
            API_CATEGORIES,
            API_INVENTORY,
            API_INVENTORY + "/reserve",
            API_INVENTORY + "/release"
    };

    private static final String[] ADMIN_PATCH_ENDPOINTS = {API_PRODUCTS + "/{id}"};

    private static final String[] ADMIN_PUT_ENDPOINTS = {API_INVENTORY};

    private static final String[] INTERNAL_USE_ONLY_ENDPOINTS = {
            API_PRODUCTS + "/batch",
            API_INVENTORY + "/reserve/batch",
            API_INVENTORY + "/release/batch"
    };

    private final AppSecurityProperties properties;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  GlobalCorsProperties corsProperties) {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.setCorsConfigurations(corsProperties.getCorsConfigurations());

        return http
                .authorizeExchange(exchange -> exchange
                        // preflight requests
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(INTERNAL_USE_ONLY_ENDPOINTS).denyAll()
                        // public endpoints
                        .pathMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS).permitAll()
                        // user endpoints
                        .pathMatchers(HttpMethod.GET, USER_GET_ENDPOINTS).hasRole(Role.USER.name())
                        .pathMatchers(HttpMethod.POST, USER_POST_ENDPOINTS).hasRole(Role.USER.name())
                        // admin endpoints
                        .pathMatchers(HttpMethod.GET, ADMIN_GET_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .pathMatchers(HttpMethod.POST, ADMIN_POST_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .pathMatchers(HttpMethod.PATCH, ADMIN_PATCH_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .pathMatchers(HttpMethod.PUT, ADMIN_PUT_ENDPOINTS).hasRole(Role.ADMIN.name())
                        .anyExchange().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(corsConfigSource))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MapReactiveUserDetailsService getUserDetailsService(PasswordEncoder encoder) {
        return new MapReactiveUserDetailsService(getSampleUsers(encoder));
    }

    private UserDetails[] getSampleUsers(PasswordEncoder encoder) {
        return properties.getUsers()
                .stream()
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(encoder.encode(user.getPassword()))
                        .roles(user.getRoles().stream().map(Role::toString).toArray(String[]::new))
                        .build())
                .toArray(UserDetails[]::new);
    }
}
