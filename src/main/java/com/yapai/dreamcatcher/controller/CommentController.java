package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.request.CreateCommentRequest;
import com.yapai.dreamcatcher.service.crud.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add-comment")
    public ResponseEntity<CommentDto> addComment(Authentication authentication,
                                                 @RequestBody CreateCommentRequest createCommentRequest) {
        CommentDto commentDto = commentService.addComment(authentication, createCommentRequest);
        return ResponseEntity.ok(commentDto);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<String> deleteComment(Authentication authentication,
                                                @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(authentication, commentId);
        return ResponseEntity.ok("Comment deleted");
    }
}
