package com.example.Logitech.repository;

import com.example.Logitech.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //상품별 리뷰 목록
    List<Review> findByItem_ItemId(Long itemId);

    //회원별 리뷰 목록
    List<Review> findByMember_MemberId(Long memberId);

    //주문별 리뷰 존재 여부
    boolean existsByOrder_OrderIdAndItem_ItemId(Long orderId, Long itemId);
}
