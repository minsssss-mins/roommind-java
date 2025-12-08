package com.roomgenius.furniture_recommendation.service;

import com.roomgenius.furniture_recommendation.entity.CartDTO;
import com.roomgenius.furniture_recommendation.entity.CartVO;
import com.roomgenius.furniture_recommendation.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    /** 공통: cartId로 조회 + 본인 것인지 확인 */
    private CartVO getUserCartOrThrow(int userId, int cartId) {

        if (cartId <= 0) {
            throw new IllegalArgumentException("잘못된 cartId 입니다.");
        }

        CartVO cart = cartMapper.findByCartId(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다.");
        }

        if (cart.getUserId() == null) {
            throw new IllegalStateException("장바구니 데이터가 잘못되었습니다.");
        }

        if (cart.getUserId() != userId) {
            throw new AccessDeniedException("본인의 장바구니만 수정/삭제할 수 있습니다.");
        }

        return cart;
    }

    /** 장바구니 추가 */
    @Override
    public void addToCart(int userId, CartDTO dto) {

        if (dto.getProductId() == null || dto.getProductId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }

        if (dto.getProductCount() == null || dto.getProductCount() <= 0) {
            throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
        }

        CartVO existing = cartMapper.findByUserAndProduct(userId, dto.getProductId());

        try {
            if (existing != null) {
                existing.setProductCount(existing.getProductCount() + dto.getProductCount());
                cartMapper.updateCount(existing);
            } else {
                CartVO vo = new CartVO();
                vo.setUserId(userId);
                vo.setProductId(dto.getProductId());
                vo.setProductCount(dto.getProductCount());
                vo.setSelectedOption(dto.getSelectedOption());

                cartMapper.insert(vo);
            }
        } catch (Exception e) {
            log.error("장바구니 추가 오류", e);
            throw new RuntimeException("장바구니 추가 중 오류가 발생했습니다.");
        }
    }

    /** 장바구니 조회 */
    @Override
    public List<CartDTO> getCart(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID입니다.");
        }

        return cartMapper.findByUserId(userId);  // DTO 그대로 받기
    }


    /** 수량 변경 */
    @Override
    public void updateCount(int userId, int cartId, int productCount) {

        if (productCount <= 0) {
            throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
        }

        CartVO cart = getUserCartOrThrow(userId, cartId);

        try {
            cart.setProductCount(productCount);
            cartMapper.updateCount(cart);
        } catch (Exception e) {
            log.error("장바구니 수량 변경 오류", e);
            throw new RuntimeException("수량 변경 중 오류가 발생했습니다.");
        }
    }

    /** 개별 삭제 */
    @Override
    public void deleteCartItem(int userId, int cartId) {

        CartVO cart = getUserCartOrThrow(userId, cartId);

        try {
            cartMapper.delete(cartId);
        } catch (Exception e) {
            log.error("장바구니 항목 삭제 오류", e);
            throw new RuntimeException("장바구니 삭제 중 오류가 발생했습니다.");
        }
    }

    /** 전체 삭제 */
    @Override
    public void clearCart(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("로그인된 사용자 정보가 잘못되었습니다.");
        }

        try {
            cartMapper.clearUserCart(userId);
        } catch (Exception e) {
            log.error("장바구니 전체 삭제 오류", e);
            throw new RuntimeException("장바구니 전체 삭제 중 오류가 발생했습니다.");
        }
    }
}
