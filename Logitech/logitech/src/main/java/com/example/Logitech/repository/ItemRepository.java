package com.example.Logitech.repository;


import com.example.Logitech.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {


    //상품명 검색(Containing 으로 중복 검색 가능하게)
    List<Item> findByItemNameContaining(String name);


}
