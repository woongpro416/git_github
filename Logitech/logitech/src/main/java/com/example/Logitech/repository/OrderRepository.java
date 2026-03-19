package com.example.Logitech.repository;


import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //회원별 주문 목록 조회
    List<Order> findByMember(Member member);
}
