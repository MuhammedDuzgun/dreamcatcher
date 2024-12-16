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
import java.util.Optional;

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
        Optional<User> user = userRepository.findByEmail(email);

        //Dream
        Optional<Dream> dream = dreamRepository.findById(createCommentRequest.getDreamId());

        //Comment
        Comment comment = new Comment();
        comment.setComment(createCommentRequest.getComment());
        comment.setDream(dream.get());
        comment.setUser(user.get());

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Authentication authentication, Long commentId) {
        //User
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();
        String email = oauth2User.getAttribute("email");
        Optional<User> user = userRepository.findByEmail(email);

        Optional<Comment> commentToDelete = commentRepository.findById(commentId);
        if (commentToDelete.get().getUser().getId().equals(user.get().getId())) {
            commentRepository.delete(commentToDelete.get());
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
