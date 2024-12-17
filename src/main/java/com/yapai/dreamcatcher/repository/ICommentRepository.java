package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.Comment;
import com.yapai.dreamcatcher.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDream(Dream dream);
}
