package com.example.Logitech.dto;

import com.example.Logitech.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long memberId;
    private String memberName;
    private Long itemId;
    private String itemName;
    private Long orderId;
    private int rating;
    private String content;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.memberId = review.getMember().getMemberId();
        this.memberName = review.getMember().getName();
        this.itemId = review.getItem().getItemId();
        this.itemName = review.getItem().getItemName();
        this.orderId = review.getOrder().getOrderId();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
