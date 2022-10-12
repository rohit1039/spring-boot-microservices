package com.services.orderservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.services.orderservice.config.OrderServiceConfig;
import com.services.orderservice.dao.OrderRepository;
import com.services.orderservice.entity.Order;
import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.model.OrderDTO;
import com.services.orderservice.proxy.model.ProductDTO;
import com.services.orderservice.proxy.model.TransactionDetails;
import com.services.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
public class OrderControllerTest
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @RegisterExtension
    static WireMockExtension wireMockServer =
            WireMockExtension.newInstance()
                    .options(WireMockConfiguration.wireMockConfig().port(8080)).build();

    private ObjectMapper objectMapper
            = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @BeforeEach
    void setUp() throws IOException
    {
        getProductDetailsResponse();
        doPayment();
        getTransactionDetails();
        reduceQuantity();
    }

    private void reduceQuantity()
    {
        circuitBreakerRegistry.circuitBreaker("external").reset();
        wireMockServer.stubFor(put(urlMatching("/product/reduce-quantity/.*")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getTransactionDetails() throws IOException
    {
        circuitBreakerRegistry.circuitBreaker("external").reset();
        wireMockServer.stubFor(get(urlMatching("/payment/.*")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetPayment.json"),
                        defaultCharset()
                ))));
    }

    private void doPayment()
    {
        wireMockServer.stubFor(post(urlEqualTo("/payment")).willReturn(aResponse()
                .withStatus(HttpStatus.CREATED.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getProductDetailsResponse() throws IOException
    {
        wireMockServer.stubFor(get("/product/1").willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetProduct.json"),
                        defaultCharset()
                ))));
    }

    private TransactionDetails getMockTransactionDetails()
    {
        return TransactionDetails.builder()
                .orderId(getMockOrder().getId())
                .paymentId(1L)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .totalAmount(160000D)
                .referenceNumber(UUID.randomUUID().toString())
                .build();

    }

    private OrderDTO getMockOrderDTO()
    {
        return OrderDTO.builder()
                .id(1L)
                .orderDate(LocalDateTime.now())
                .paymentMode(getMockOrder().getPaymentMode())
                .orderStatus(getMockOrder().getOrderStatus())
                .price(getMockOrder().getPrice())
                .quantity(getMockOrder().getQuantity())
                .totalAmount(getMockOrder().getTotalAmount())
                .transactionDetails(getMockTransactionDetails())
                .productDTO(getMockProductDTO())
                .build();
    }

    private ProductDTO getMockProductDTO()
    {
        return ProductDTO.builder()
                .productId(1L)
                .productName("iPhone 13")
                .price(80000D)
                .quantity(2L)
                .build();
    }

    private Order getMockOrder()
    {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER_PLACED)
                .orderDate(LocalDateTime.now())
                .price(45000D)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .id(1L)
                .quantity(2L)
                .totalAmount(45000D * 2L)
                .productId(2L)
                .build();
    }

    @DisplayName("Place Order and Payment - Success Scenario")
    @Test
    public void test_Place_Order_Do_Payment_Success() throws Exception
    {
        // get OrderDTO
        OrderDTO orderDTO = getMockOrderDTO();

        // call the placeOrder end point along with jwt
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/order/product/{productId}", 2L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        //get the response as string
        String result = mvcResult.getResponse().getContentAsString();

        //read the response and set it to orderDTO class
        OrderDTO dto = objectMapper.readValue(result, OrderDTO.class);

        //check if the order saved in the database
        Optional<Order> order = this.orderRepository.findById(dto.getId());

        assertTrue(order.isPresent());

        Order o = order.get();

        assertEquals(dto.getId(), o.getId());

        assertEquals(dto.getOrderStatus(), o.getOrderStatus());

        assertEquals(dto.getTotalAmount(), o.getTotalAmount());
    }

    @DisplayName("UnAuthorized - Failure Scenario")
    @Test
    public void test_When_UnAuthorized_Throw403() throws Exception
    {
        // get OrderDTO
        OrderDTO orderDTO = getMockOrderDTO();

        // call the placeOrder end point along with jwt but as Admin not Customer
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/order/product/{productId}", 2L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @DisplayName("Get Order Details - Success Scenario")
    @Test
    public void test_GetOrderById_Success() throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/order/get/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        //get the response as string
        String actualResponse = mvcResult.getResponse().getContentAsString();

        //read the response and set it to orderDTO class
        OrderDTO actualDTO = objectMapper.readValue(actualResponse, OrderDTO.class);

        //check order exist or not in database
        Order order = this.orderRepository.findById(1L).get();

        //get expectedResponse
        OrderDTO expectedDTO = getOrderResponse(order);

        assertEquals(expectedDTO, actualDTO);
    }

    private OrderDTO getOrderResponse(Order order) throws IOException
    {
        OrderDTO transactionDetails = OrderDTO.builder().transactionDetails(objectMapper.readValue(
                copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetPayment.json"), defaultCharset()), TransactionDetails.class)).build();

        OrderDTO productDTO = OrderDTO.builder().productDTO(objectMapper.readValue(
                copyToString(OrderControllerTest.class.getClassLoader().getResourceAsStream("mock/GetProduct.json"), defaultCharset()).getBytes(), ProductDTO.class)).build();

        OrderDTO response = OrderDTO.builder()
                .id(order.getId())
                .transactionDetails(transactionDetails.getTransactionDetails())
                .productDTO(productDTO.getProductDTO())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .build();

        return response;
    }


}