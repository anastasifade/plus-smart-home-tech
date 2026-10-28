package ru.yandex.practicum.gateway.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    RouterFunction<ServerResponse> getRouterFunction() {
        return RouterFunctions.route(RequestPredicates.all(),
                request -> ServerResponse.ok().build());
    }
}
