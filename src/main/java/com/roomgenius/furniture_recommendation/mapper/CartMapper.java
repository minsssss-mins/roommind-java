package com.roomgenius.furniture_recommendation.mapper;

import com.roomgenius.furniture_recommendation.entity.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    CartVO findByUserAndProduct(@Param("userId") int userId,
                                @Param("productId") int productId);

    List<CartVO> findByUserId(@Param("userId") int userId);

    int insert(CartVO cart);

    int updateCount(CartVO cart);

    int delete(@Param("cartId") int cartId);

    int clearUserCart(@Param("userId") int userId);

    CartVO findByCartId(@Param("cartId") int cartId);
}
