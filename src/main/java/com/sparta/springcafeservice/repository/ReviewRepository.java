package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    //기본적인 JpaRepository의 SimpleJpaRepository 클래스에서 제네릭 다형성 활용 <T or ?> 타입으로 정의되어있다.
    //List<Comment> findAllByCommentIdOrderByStoreId(Long storeId); // JpaMethod는 정렬기준 바꾸거나 where등 조건들 필요할때

}