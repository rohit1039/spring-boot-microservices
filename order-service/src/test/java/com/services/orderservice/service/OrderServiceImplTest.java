package com.services.orderservice.service;

import com.services.orderservice.dao.OrderRepository;
import com.services.orderservice.entity.Order;
import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.exception.CustomException;
import com.services.orderservice.model.OrderDTO;
import com.services.orderservice.proxy.model.ProductDTO;
import com.services.orderservice.proxy.model.TransactionDetails;
import com.services.orderservice.proxy.client.PaymentService;
import com.services.orderservice.proxy.client.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest
{
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductService productService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get Order by orderId - Success Scenario")
    @Test
    public void test_GetOrderById_Success()
    {
        Order order = getMockOrder();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductDTO.class
        )).thenReturn(getMockProductDTO());

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/" + order.getId(), TransactionDetails.class
        )).thenReturn(getMockTransactionDetails());

        OrderDTO orderDTO = orderService.getOrderById(1L);

        verify(orderRepository, times(1)).findById(anyLong());

        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductDTO.class
        );

        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/payment/" + order.getId(), TransactionDetails.class
        );

        assertNotNull(orderDTO);

        assertEquals(order.getId(), orderDTO.getId());
    }

    private TransactionDetails getMockTransactionDetails()
    {
        return TransactionDetails.builder()
                .orderId(1L)
                .paymentId(1L)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .totalAmount(160000D)
                .referenceNumber(UUID.randomUUID().toString())
                .build();

    }

    private ProductDTO getMockProductDTO()
    {
        return ProductDTO.builder()
                .productId(2L)
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
                .id(1L)
                .quantity(2L)
                .totalAmount(45000D * 2L)
                .productId(2L)
                .build();
    }

    @DisplayName("Get Order by orderId - Failure Scenario")
    @Test
    public void test_GetOrderById_Failure()
    {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        CustomException customException = assertThrows(CustomException.class,
                () -> orderService.getOrderById(1L));

        assertEquals("404 NOT_FOUND", customException.getErrorCode());

        assertEquals(404, customException.getStatus());

        verify(orderRepository, times(1)).findById(anyLong());
    }


    @DisplayName("Place Order - Success Scenario")
    @Test
    public void test_Order_Success_And_Payment_Success()
    {
        Order order = getMockOrder();

        OrderDTO orderDTO = getMockOrderDTO();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Object>(ProductDTO.class, HttpStatus.OK));

        when(paymentService.doPayment(any(TransactionDetails.class))).thenReturn(new ResponseEntity<Object>(TransactionDetails.class, HttpStatus.OK));

        OrderDTO dto = orderService.orderNow(orderDTO,1L);

        verify(orderRepository, times(2)).save(any());

        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());

        verify(paymentService, times(1)).doPayment(any(TransactionDetails.class));

        assertNotNull(dto);
    }

    private OrderDTO getMockOrderDTO()
    {
        return OrderDTO.builder()
                .id(getMockOrder().getId())
                .orderDate(getMockOrder().getOrderDate())
                .paymentMode(getMockOrder().getPaymentMode())
                .orderStatus(getMockOrder().getOrderStatus())
                .price(getMockOrder().getPrice())
                .quantity(getMockOrder().getQuantity())
                .totalAmount(getMockOrder().getTotalAmount())
                .transactionDetails(getMockTransactionDetails())
                .productDTO(getMockProductDTO())
                .build();
    }

    @DisplayName("Place Order - Failure Scenario")
    @Test
    public void test_When_Order_Success_And_Payment_Fails()
    {
        Order order = getMockOrder();

        OrderDTO orderDTO = getMockOrderDTO();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Object>(ProductDTO.class, HttpStatus.OK));

        when(paymentService.doPayment(any(TransactionDetails.class))).thenThrow(new RuntimeException());

        orderService.orderNow(orderDTO,1L);

        verify(orderRepository, times(2)).save(any());

        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());

        verify(paymentService, times(1)).doPayment(any(TransactionDetails.class));

        assertEquals(OrderStatus.ORDER_FAILED,order.getOrderStatus());
    }
}