package com.example.Logitech.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private Long memberId;
    private String address;
    private String payment;
    private int amount;
    private List<Long> itemIds;
}