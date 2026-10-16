package ru.yandex.practicum.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.common.ProductDto;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products")
    Set<ProductDto> getProductsById(@RequestParam("ids") Set<Long> ids);

}
