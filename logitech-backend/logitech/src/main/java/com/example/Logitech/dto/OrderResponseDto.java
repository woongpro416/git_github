package com.example.Logitech.dto;



import com.example.Logitech.domain.Order;
import com.example.Logitech.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private Long memberId;
    private String name;
    private String address;
    private String payment;
    private int amount;
    private String itemIds;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    private OrderStatus status;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.memberId = order.getMember().getMemberId();
        this.name = order.getMember().getName();
        this.itemIds = order.getItemIds();
        this.address = order.getAddress();
        this.payment = order.getPayment();
        this.amount = order.getAmount();
        this.createAt = order.getCreatedAt();
        this.status = order.getStatus();
    }
}
