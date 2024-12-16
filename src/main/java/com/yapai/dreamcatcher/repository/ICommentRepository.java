package com.yapai.dreamcatcher.repository;

import com.yapai.dreamcatcher.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentRepository extends JpaRepository<Comment, Long> {

}
