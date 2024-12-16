package com.yapai.dreamcatcher.service;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.dto.CreateCommentRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ICommentService {
    CommentDto addComment(Authentication authentication, CreateCommentRequest createCommentRequest);
    void deleteComment(Authentication authentication, Long commentId);
    List<CommentDto> getAllComments();
}
