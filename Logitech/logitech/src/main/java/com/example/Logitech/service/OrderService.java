package com.example.Logitech.service;


import com.example.Logitech.domain.Item;
import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Order;
import com.example.Logitech.domain.OrderStatus;

import com.example.Logitech.dto.OrderResponseDto;
import com.example.Logitech.repository.ItemRepository;
import com.example.Logitech.repository.MemberRepository;
import com.example.Logitech.repository.OrderRepository;
import com.example.Logitech.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class OrderService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    //주문 생성
    @Transactional
    public Long createOrder(Member member, OrderRequestDto dto) {
        // 재고 차감 처리
        for (Long itemId : dto.getItemIds()) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

            if (item.getStock() < 1) {
                throw new IllegalArgumentException(item.getItemName() + "의 재고가 부족합니다.");
            }
            item.setStock(item.getStock() - 1);
        }

        String itemIds = dto.getItemIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Order order = Order.builder()
                .member(member)
                .address(dto.getAddress())
                .payment(dto.getPayment())
                .amount(dto.getAmount())
                .itemIds(itemIds)
                .status(OrderStatus.ORDER)
                .build();
        return orderRepository.save(order).getOrderId();
    }

    //주문 목록 조회
    public List<OrderResponseDto> getOrderList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return orderRepository.findByMember(member)
                .stream()
                .map(order -> new OrderResponseDto(order))
                .collect(Collectors.toList());
    }

    //주문 전체 목록 (관리자)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    //주문 상세 조회
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다."));
        return new OrderResponseDto(order);
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다."));

        // 재고 복구
        Arrays.stream(order.getItemIds().split(","))
                .map(Long::parseLong)
                .forEach(itemId -> {
                    Item item = itemRepository.findById(itemId)
                            .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
                    item.setStock(item.getStock() + 1);
                });

        order.setStatus(OrderStatus.CANCEL);
    }

}
