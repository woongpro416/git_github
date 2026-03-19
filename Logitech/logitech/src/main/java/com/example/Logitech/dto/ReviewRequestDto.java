package com.example.Logitech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long memberId;
    private Long itemId;
    private Long orderId;
    private int rating;
    private String content;
}
