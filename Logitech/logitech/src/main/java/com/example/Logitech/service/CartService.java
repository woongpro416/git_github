package com.example.Logitech.service;

import com.example.Logitech.domain.Cart;
import com.example.Logitech.domain.Item;
import com.example.Logitech.domain.Member;
import com.example.Logitech.dto.CartResponseDto;
import com.example.Logitech.repository.CartRepository;
import com.example.Logitech.repository.ItemRepository;
import com.example.Logitech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class CartService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;


    //장바구니 생성 -> MemberService에서 구현했음

    // 장바구니 전체 조회
    public List<CartResponseDto> getCartList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        return cartRepository.findByMember(member)
                .stream()
                .map(CartResponseDto::new)
                .collect(Collectors.toList());
    }

    // 수정된 장바구니 항목 단건 삭제
    @Transactional
    public void deleteCart(Long cartId) {
        // memberId가 아닌 cartId를 받아 해당 항목이 있는지 확인합니다.
        if (!cartRepository.existsById(cartId)) {
            throw new IllegalStateException("존재하지 않는 장바구니 항목입니다.");
        }
        // 해당 장바구니 항목만 삭제합니다.
        cartRepository.deleteById(cartId);
    }

    //장바구니 상품 추가
    public void addItem(Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 멤버입니다."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        Optional<Cart> existing = cartRepository.findByMemberAndItem(member, item);
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + 1);
        } else {

            Cart cart = Cart.builder()
                    .member(member)
                    .item(item)
                    .quantity(1)
                    .build();
            cartRepository.save(cart);
        }
    }
}