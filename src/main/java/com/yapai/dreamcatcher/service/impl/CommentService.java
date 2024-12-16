package com.yapai.dreamcatcher.service.impl;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.dto.CreateCommentRequest;
import com.yapai.dreamcatcher.entity.Comment;
import com.yapai.dreamcatcher.entity.Dream;
import com.yapai.dreamcatcher.entity.User;
import com.yapai.dreamcatcher.mapper.CommentMapper;
import com.yapai.dreamcatcher.repository.ICommentRepository;
import com.yapai.dreamcatcher.repository.IDreamRepository;
import com.yapai.dreamcatcher.repository.IUserRepository;
import com.yapai.dreamcatcher.service.ICommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService implements ICommentService {

    private final IUserRepository userRepository;
    private final IDreamRepository dreamRepository;
    private final ICommentRepository commentRepository;

    public CommentService(IUserRepository userRepository, IDreamRepository dreamRepository, ICommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.dreamRepository = dreamRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto addComment(Authentication authentication, CreateCommentRequest createCommentRequest) {
        //User
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        //Dream
        Dream dream = dreamRepository.findById(createCommentRequest.getDreamId())
                .orElseThrow(()-> new RuntimeException("Dream not found"));

        //Comment
        Comment comment = new Comment();
        comment.setComment(createCommentRequest.getComment());
        comment.setDream(dream);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Authentication authentication, Long commentId) {
        //User
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException("Comment not found"));

        if (commentToDelete.getUser().getId().equals(user.getId())) {
            commentRepository.delete(commentToDelete);
        }
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDto> commentDtos = new ArrayList<>();
        if (!comments.isEmpty()) {
            comments.stream().map(CommentMapper::mapToCommentDto).forEach(commentDtos::add);
        }
        return commentDtos;
    }

}
