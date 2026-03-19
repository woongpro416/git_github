package com.example.Logitech.repository;

import com.example.Logitech.domain.Cart;
import com.example.Logitech.domain.Item;
import com.example.Logitech.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    void deleteByMember(Member member);

    List<Cart> findByMember(Member member);

    //회원 + 상품으로 기존 장바구니 항목 조회
    Optional<Cart> findByMemberAndItem(Member member, Item item);

}