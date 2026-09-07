package com.example.order.service;

import com.example.customer.domain.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.order.domain.OrderEntity;
import com.example.order.domain.OrderItem;
import com.example.order.enums.OrderStatus;
import com.example.order.dto.request.CreateOrderRequest;
import com.example.order.dto.request.OrderItemRequest;
import com.example.order.repository.OrderRepository;
import com.example.product.domain.Product;
import com.example.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderEntity createOrder(Long customerId, CreateOrderRequest request) {

        // 1. 주문할 고객 조회
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // 2. 주문 생성
        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.ORDERED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. 요청으로 들어온 상품들을 하나씩 처리
        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // 상품 가격은 프론트가 아니라 DB에서 가져옴
            BigDecimal price = product.getPrice();

            // OrderItem 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(price);

            // Order와 OrderItem 연결
            order.addItem(orderItem);

            // 총 주문 금액 계산
            BigDecimal itemTotal =
                    price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);
        }

        // 4. 총 금액 저장
        order.setTotalAmount(totalAmount);

        // 5. 주문 + OrderItem들을 DB에 저장
        return orderRepository.save(order);
    }
}