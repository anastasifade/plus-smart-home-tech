package ru.yandex.practicum.order.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RequestIdConfig {
    private final CustomHeaders headers;

    @Bean
    public RequestInterceptor requestIdInterceptor() {
        return template -> {
            String requestId = getCurrentRequestId();
            template.header(headers.getRequestIdHeader(), requestId);
        };
    }

    private String getCurrentRequestId() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return UUID.randomUUID().toString();
        }

        HttpServletRequest request = attributes.getRequest();
        String requestId = request.getHeader(headers.getRequestIdHeader());

        if (requestId == null || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return requestId;
    }
}
