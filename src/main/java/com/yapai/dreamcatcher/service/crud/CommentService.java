package com.yapai.dreamcatcher.service.crud;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.request.CreateCommentRequest;
import com.yapai.dreamcatcher.entity.Comment;
import com.yapai.dreamcatcher.entity.Dream;
import com.yapai.dreamcatcher.entity.User;
import com.yapai.dreamcatcher.exception.ResourceNotFoundException;
import com.yapai.dreamcatcher.mapper.CommentMapper;
import com.yapai.dreamcatcher.repository.ICommentRepository;
import com.yapai.dreamcatcher.repository.IDreamRepository;
import com.yapai.dreamcatcher.repository.IUserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final IUserRepository userRepository;
    private final IDreamRepository dreamRepository;
    private final ICommentRepository commentRepository;

    public CommentService(IUserRepository userRepository,
                          IDreamRepository dreamRepository,
                          ICommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.dreamRepository = dreamRepository;
        this.commentRepository = commentRepository;
    }

    public CommentDto addComment(Authentication authentication, CreateCommentRequest createCommentRequest) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        //User
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user not found"));

        //Dream
        Dream dream = dreamRepository.findById(createCommentRequest.dreamId())
                .orElseThrow(()-> new ResourceNotFoundException("dream not found"));

        //Comment
        Comment comment = new Comment();
        comment.setComment(createCommentRequest.comment());
        comment.setDream(dream);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(savedComment);
    }

    public void deleteComment(Authentication authentication, Long commentId) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        //User
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user not found"));

        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("comment not found"));

        if (commentToDelete.getUser().getId().equals(user.getId())) {
            commentRepository.delete(commentToDelete);
        }
    }

    public List<CommentDto> getAllCommentsByDreamId(Long dreamId) {
        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(()-> new ResourceNotFoundException("dream not found"));
        List<Comment> comments = commentRepository.findByDream(dream);
        List<CommentDto> commentDtos = new ArrayList<>();
        if (!comments.isEmpty()) {
            comments.stream().map(CommentMapper::mapToCommentDto).forEach(commentDtos::add);
        }
        return commentDtos;
    }
}
