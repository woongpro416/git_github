package com.example.Logitech.service;

import com.example.Logitech.domain.Item;
import com.example.Logitech.dto.ItemFormDto;
import com.example.Logitech.dto.ItemResponseDto;
import com.example.Logitech.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class ItemService {

    private final ItemRepository itemRepository;

    //상품 목록(다건 검색 조회 포함)
    public List<ItemResponseDto> getItemList(String itemName) {
        List<Item> items;
        if (itemName == null || itemName.isEmpty()) {
            items = itemRepository.findAll();  // 전체 목록
        } else {
            items = itemRepository.findByItemNameContaining(itemName);  // 이름 검색
        }
        return items.stream()
                .map(item -> new ItemResponseDto(item))
                .collect(Collectors.toList());
    }


    //상품 상세보기
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        return new ItemResponseDto(item);
    }

    //상품 등록 처리
    public Long saveItem(ItemFormDto dto) {
        Item item = Item.builder()
                .itemName(dto.getItemName())
                .imgPath(dto.getImgPath())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(dto.getCategory())
                .build();
        return itemRepository.save(item).getItemId();
    }

    //상품 수정 처리
    @Transactional
    public void updateItem(Long itemId, ItemFormDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        item.setItemName(dto.getItemName());
        item.setImgPath(dto.getImgPath());
        item.setPrice(dto.getPrice());
        item.setStock(dto.getStock());
        item.setCategory(dto.getCategory());
    }

    //상품 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        itemRepository.deleteById(itemId);
    }

    //재고 수정
    @Transactional
    public void updateStock(Long itemId, int stock) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        item.setStock(item.getStock());
        itemRepository.save(item);
    }
}
