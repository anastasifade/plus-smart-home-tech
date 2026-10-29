package ru.yandex.practicum.gateway.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureWebTestClient
@Import(TestSecurityConfig.class)
public class SecurityTests {

    private static final String API_PRODUCTS = "/api/products";
    private static final String API_CATEGORIES = "/api/categories";
    private static final String API_INVENTORY = "/api/inventory";
    private static final String API_ORDERS = "/api/orders";
    private static final String TEST_USER = "ivan";
    private static final String TEST_ADMIN = "anna";

    @Autowired
    private WebTestClient client;


    // API PRODUCTS

    @Test
    void test_getProducts_isAvailableWithoutAuthentication() {
        client.get()
                .uri(API_PRODUCTS)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getProductById_isAvailableWithoutAuthentication() {
        client.get()
                .uri(API_PRODUCTS + "/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_createProducts_whileUnauthenticated_isUnauthorized() {
        client.post()
                .uri(API_PRODUCTS)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_createProducts_withRoleAdmin_isOk() {
        client.post()
                .uri(API_PRODUCTS)
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_createProducts_withRoleUser_isForbidden() {
        client.post()
                .uri(API_PRODUCTS)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_modifyProducts_whileUnauthenticated_isUnauthorized() {
        client.patch()
                .uri(API_PRODUCTS + "/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_modifyProducts_withRoleUser_isForbidden() {
        client.patch()
                .uri(API_PRODUCTS + "/1")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_modifyProducts_withRoleAdmin_isOk() {
        client.patch()
                .uri(API_PRODUCTS + "/1")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getProducts_batchEndpoint_whenUnauthenticated_isUnauthorized() {
        client.get()
                .uri(API_PRODUCTS + "/batch")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_getProducts_batchEndpoint_withRoleUser_isForbidden() {
        client.get()
                .uri(API_PRODUCTS + "/batch")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_getProducts_batchEndpoint_withRoleAdmin_isForbidden() {
        client.get()
                .uri(API_PRODUCTS + "/batch")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isForbidden();
    }

    // API CATEGORIES

    @Test
    void test_getCategories_isAvailableWithoutAuthentication() {
        client.get()
                .uri(API_CATEGORIES)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getCategoryById_isAvailableWithoutAuthentification() {
        client.get()
                .uri(API_CATEGORIES + "/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_createCategory_withoutAuthentication_isUnauthorized() {
        client.post()
                .uri(API_CATEGORIES)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_createCategory_withRoleUser_isForbidden() {
        client.post()
                .uri(API_CATEGORIES)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_createCategory_withRoleAdmin_isOk() {
        client.post()
                .uri(API_CATEGORIES)
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }


    // API INVENTORY

    @Test
    void test_getInventory_isAvailableWithoutAuthentication() {
        client.get()
                .uri(API_INVENTORY)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getInventoryItemById_isAvailableWithoutAuthentification() {
        client.get()
                .uri(API_INVENTORY + "/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_createInventoryItem_withoutAuthentication_isUnauthorized() {
        client.post()
                .uri(API_INVENTORY)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_reserveInventory_withoutAuthentification_isUnauthorized() {
        client.post()
                .uri(API_INVENTORY + "/reserve")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_releaseReservation_withoutAuthentification_isUnauthorized() {
        client.post()
                .uri(API_INVENTORY + "/release")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_createInventoryItem_withRoleUser_isForbidden() {
        client.post()
                .uri(API_INVENTORY)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_reserveItem_withRoleUser_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/reserve")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_releaseReservation_withRoleUser_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/release")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_createInventoryItem_withRoleAdmin_isOk() {
        client.post()
                .uri(API_INVENTORY)
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_reserveItem_withRoleAdmin_isOk() {
        client.post()
                .uri(API_INVENTORY + "/reserve")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_releaseReservation_withRoleAdmin_isOk() {
        client.post()
                .uri(API_INVENTORY + "/release")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_modifyInventory_withoutAuthentication_isUnauthorized() {
        client.put()
                .uri(API_INVENTORY)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_modifyInventory_withRoleUser_isForbidden() {
        client.put()
                .uri(API_INVENTORY)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_modifyInventory_withRoleAdmin_isOk() {
        client.put()
                .uri(API_INVENTORY)
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_batchReserveEndpoint_withoutAuthentification_isUnauthorized() {
        client.post()
                .uri(API_INVENTORY + "/reserve/batch")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_batchReserveEndpoint_withRoleUser_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/reserve/batch")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_batchReserveEndpoint_withRoleAdmin_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/reserve/batch")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_batchReleaseEndpoint_withoutAuthentification_isUnauthorized() {
        client.post()
                .uri(API_INVENTORY + "/release/batch")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_batchReleaseEndpoint_withRoleUser_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/release/batch")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_batchReleaseEndpoint_withRoleAdmin_isForbidden() {
        client.post()
                .uri(API_INVENTORY + "/release/batch")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isForbidden();
    }


    // API ORDERS

    @Test
    void test_getOrders_withoutAuthentication_isUnauthorized() {
        client.get()
                .uri(API_ORDERS)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_getOrderById_withoutAuthentification_isUnauthorized() {
        client.get()
                .uri(API_ORDERS + "/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_getOrderByEmail_withoutAuthentification_isUnauthorized() {
        client.get()
                .uri(API_ORDERS + "/by-email")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_getOrdersByEmail_withRoleUser_isOk() {
        client.get()
                .uri(API_ORDERS + "/by-email")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getOrderById_withRoleUser_isOk() {
        client.get()
                .uri(API_ORDERS + "/1")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_getAllOrders_withRoleUser_isForbidden() {
        client.get()
                .uri(API_ORDERS)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_getAllOrders_withRoleAdmin_isOk() {
        client.get()
                .uri(API_ORDERS)
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_createOrder_withoutAuthentication_isUnauthorized() {
        client.post()
                .uri(API_ORDERS)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_createOrder_whileAuthenticated_isPermitted() {
        client
                .post()
                .uri(API_ORDERS)
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isOk();
    }


    // OTHER

    @Test
    void test_preflight_isNotBlockedBySecurity() {
        client
                .options()
                .uri("http://localhost:8080" + API_ORDERS)
                .header(HttpHeaders.ORIGIN, "http://localhost:8443")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "authorization, content-type")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void test_unknownEndpoint_withoutAuthentification_isUnauthorized() {
        client.get()
                .uri(API_INVENTORY + "/unknown/12345")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void test_unknownEndpoint_withRoleUser_isForbidden() {
        client.get()
                .uri(API_INVENTORY + "/unknown/12345")
                .headers(headers -> headers.setBasicAuth(TEST_USER, TEST_USER))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void test_unknownEndpoint_withRoleAdmin_isForbidden() {
        client.get()
                .uri(API_INVENTORY + "/unknown/12345")
                .headers(headers -> headers.setBasicAuth(TEST_ADMIN, TEST_ADMIN))
                .exchange()
                .expectStatus().isForbidden();
    }

}
