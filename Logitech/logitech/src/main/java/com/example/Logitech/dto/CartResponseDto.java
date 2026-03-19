package com.example.Logitech.dto;

import com.example.Logitech.domain.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private Long cartId;

    private Long itemId;

    private String itemName;

    private String imgPath;

    private int price;

    private int quantity;

    //Entity -> Dto
    public CartResponseDto(Cart cart) {
        this.cartId = cart.getCartId();
        this.itemName = cart.getItem().getItemName();
        this.itemId = cart.getItem().getItemId();
        this.imgPath = cart.getItem().getImgPath();
        this.price = cart.getItem().getPrice();
        this.quantity = cart.getQuantity();
    }
}
