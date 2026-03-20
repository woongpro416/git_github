package com.example.Logitech.domain;

import com.example.Logitech.dto.ItemResponseDto;
import com.example.Logitech.repository.ItemRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "items")

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(length = 50, nullable = false)
    private String itemName;

    @Column(length = 150, nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private int price;

    @CreationTimestamp
    private LocalDateTime createAt;

    //재고 수량
    @Column(nullable = false)
    private int stock;

    //신상품/베스트/추천상품 자동 표출
    @Enumerated(EnumType.STRING)
    private ItemCategory category;


}
