package com.example.Logitech.service;

import com.example.Logitech.domain.Item;
import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Order;
import com.example.Logitech.domain.Review;
import com.example.Logitech.dto.ReviewRequestDto;
import com.example.Logitech.dto.ReviewResponseDto;
import com.example.Logitech.repository.ItemRepository;
import com.example.Logitech.repository.MemberRepository;
import com.example.Logitech.repository.OrderRepository;
import com.example.Logitech.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    //상품별 리뷰 목록
    public List<ReviewResponseDto> getReviewByItem(Long itemId) {
        return reviewRepository.findByItem_ItemId(itemId)
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //회원별 리뷰 목록
    public List<ReviewResponseDto> getReviewByMember(Long memberId) {
        return reviewRepository.findByMember_MemberId(memberId)
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //전체 리뷰 목록 (관리자)
    public List<ReviewResponseDto> getAllReview() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //리뷰 등록
    @Transactional
    public Long saveReview(ReviewRequestDto dto) {
        if (reviewRepository.existsByOrder_OrderIdAndItem_ItemId(dto.getOrderId(),dto.getItemId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성하셨습니다.");
        }
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다."));

        Review review = Review.builder()
                .member(member)
                .item(item)
                .order(order)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();
        return reviewRepository.save(review).getReviewId();
    }

    //리뷰 수정
    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 리뷰입니다."));
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 리뷴입니다."));
        reviewRepository.deleteById(reviewId);
    }
}
