package com.roomgenius.furniture_recommendation.repository;

import com.roomgenius.furniture_recommendation.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    // 커스텀 쿼리 메서드가 필요하면 여기에 작성
}
