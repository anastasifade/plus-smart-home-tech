package ru.yandex.practicum.order.exception.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommerceFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder decoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
       return switch (response.status()) {
            case 400 -> new BadRequestException("Could not process the request.");
            case 404 -> new NotFoundException("Resource not found.");
            case 409 -> new ConflictException("Conflict.");
            case 422 -> new OrderProcessingException("Could not process the request.");
            case 500, 501, 502, 503, 504 -> new ServiceUnavailableException("Order could not be processed. Try again later.");
            default -> decoder.decode(methodKey, response);
        };
    }
}
