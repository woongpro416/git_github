package com.example.Logitech.dto;

import com.example.Logitech.domain.Item;
import com.example.Logitech.domain.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ItemResponseDto {

    private Long itemId;

    private String itemName;

    private String imgPath;

    private int price;

    private LocalDateTime createdAt;

    private int stock;

    private ItemCategory category;

    //Entity -> DTO 변환

    public ItemResponseDto(Item item) {
        this.itemId = item.getItemId();
        this.itemName = item.getItemName();
        this.imgPath = item.getImgPath();
        this.price = item.getPrice();
        this.createdAt = item.getCreateAt();
        this.stock = item.getStock();
        this.category = item.getCategory();
    }
}
