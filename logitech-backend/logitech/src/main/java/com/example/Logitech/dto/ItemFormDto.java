package com.example.Logitech.dto;

import com.example.Logitech.domain.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItemFormDto {

    private String itemName;

    private String imgPath;

    private int price;

    private int stock;

    private ItemCategory category;
}
