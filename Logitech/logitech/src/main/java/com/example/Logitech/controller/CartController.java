package com.example.Logitech.controller;


import com.example.Logitech.dto.CartResponseDto;
import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")

public class CartController {

    private final CartService cartService;

    // 이용자의 장바구니 조회
    @GetMapping("/list")
    public ResponseEntity<List<CartResponseDto>> getCartList(HttpSession session) {
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(cartService.getCartList(loginMember.getMemberId()));
    }

    // 장바구니 삭제 (memberId 기준 전체 삭제)
    @DeleteMapping("/delete/{cartId}")
    public void deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
    }

    //장바구니에 추가
    @PostMapping("/add")
    public void addItem(@RequestBody Map<String, Long>body, HttpSession session) {
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute("loginMember");
        cartService.addItem(loginMember.getMemberId(), body.get("itemId"));
    }
}
