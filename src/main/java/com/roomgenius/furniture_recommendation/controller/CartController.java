package com.roomgenius.furniture_recommendation.controller;

import com.roomgenius.furniture_recommendation.entity.CartDTO;
import com.roomgenius.furniture_recommendation.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private int getLoginUserId(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        return userId;
    }

    /** 장바구니 담기 */
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CartDTO dto,
                                 HttpServletRequest request) {
        int userId = getLoginUserId(request);
        cartService.addToCart(userId, dto);
        return ResponseEntity.ok("장바구니에 추가되었습니다.");
    }

    /** 장바구니 조회 */
    @GetMapping
    public ResponseEntity<List<CartDTO>> list(HttpServletRequest request) {
        int userId = getLoginUserId(request);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /** 수량 변경 */
    @PatchMapping("/{cartId}")
    public ResponseEntity<?> updateCount(@PathVariable int cartId,
                                         @RequestBody CartDTO dto,
                                         HttpServletRequest request) {
        int userId = getLoginUserId(request);
        cartService.updateCount(userId, cartId, dto.getProductCount());
        return ResponseEntity.ok("수량이 변경되었습니다.");
    }

    /** 개별 삭제 */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> delete(@PathVariable int cartId,
                                    HttpServletRequest request) {
        int userId = getLoginUserId(request);
        cartService.deleteCartItem(userId, cartId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    /** 전체 삭제 */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clear(HttpServletRequest request) {
        int userId = getLoginUserId(request);
        cartService.clearCart(userId);
        return ResponseEntity.ok("장바구니가 비워졌습니다.");
    }
}
