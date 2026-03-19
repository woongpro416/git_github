package com.example.Logitech.controller;

import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.dto.OrderResponseDto;
import com.example.Logitech.repository.ItemRepository;
import com.example.Logitech.repository.MemberRepository;
import com.example.Logitech.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    //대시보드 통계
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> data = new HashMap<>();

        //통계 숫자
        data.put("totalMembers", memberRepository.count());
        data.put("totalOrders", orderRepository.count());
        data.put("totalItems", itemRepository.count());

        //최근 가입한 5명
        data.put("recentMembers", memberRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreateAt().compareTo(a.getCreateAt()))
                .limit(5)
                .map(MemberResponseDto::new)
                .collect(Collectors.toList()));
        //최근 주문 5건
        data.put("recentOrders", orderRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(OrderResponseDto::new)
                .collect(Collectors.toList()));

        return data;
    }
}
