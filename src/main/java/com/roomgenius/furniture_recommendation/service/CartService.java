package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CartDTO;

import java.util.List;

public interface CartService {

    // 장바구니 담기
    void addToCart(int userId, CartDTO dto);

    // 장바구니 조회
    List<CartDTO> getCart(int userId);

    // 수량 변경
    void updateCount(int userId, int cartId, int productCount);

    // 개별 삭제
    void deleteCartItem(int userId, int cartId);

    // 전체 삭제
    void clearCart(int userId);
}
