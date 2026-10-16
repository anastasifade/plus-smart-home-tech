package ru.yandex.practicum.order.client;

import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.common.ProductDto;

import java.util.Set;

@FeignClient(name = "product-service")
public interface ProductClient {

    @CollectionFormat(feign.CollectionFormat.CSV)
    @GetMapping("/api/products/batch")
    Set<ProductDto> getProductsByIds(@RequestParam Set<Long> ids);

}
