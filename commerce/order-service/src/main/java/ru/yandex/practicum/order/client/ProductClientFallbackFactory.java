package ru.yandex.practicum.order.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.BusinessException;
import ru.yandex.practicum.order.exception.ServiceUnavailableException;

@Slf4j
@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        if (cause instanceof BusinessException be) {
            throw be;
        }

        return productIds -> {
            log.warn(
                    "Product service unavailable when requesting products with ids={}",
                    productIds,
                    cause
            );

            throw new ServiceUnavailableException(("External service failure when requesting products with ids=" +
                    productIds), cause);
        };
    }
}
