package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.dto.CreateCommentRequest;
import com.yapai.dreamcatcher.service.ICommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
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

    @GetMapping("/all-comments")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> commentDtos = commentService.getAllComments();
        return ResponseEntity.ok(commentDtos);
    }

}
