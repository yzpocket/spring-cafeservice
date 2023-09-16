package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //List<Comment> findAllByCommentIdOrderByStoreId(Long storeId); // 필요한 정보만 가져오도록 함

}