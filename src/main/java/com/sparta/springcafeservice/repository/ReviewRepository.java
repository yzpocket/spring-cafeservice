package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //특정 가게의 리뷰 모두 조회
    List<Review> findByStoreId(Long storeId);

}
    // JpaMethod는 정렬기준 바꾸거나 where등 조건들 필요할때 카멜케이스로 작성할 수 있다.
