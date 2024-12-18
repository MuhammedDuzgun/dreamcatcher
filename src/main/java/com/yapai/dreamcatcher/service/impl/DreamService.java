package com.yapai.dreamcatcher.service.impl;

import com.yapai.dreamcatcher.dto.CreateDreamRequest;
import com.yapai.dreamcatcher.dto.DreamDto;
import com.yapai.dreamcatcher.entity.Dream;
import com.yapai.dreamcatcher.entity.User;
import com.yapai.dreamcatcher.mapper.DreamMapper;
import com.yapai.dreamcatcher.repository.IDreamRepository;
import com.yapai.dreamcatcher.repository.IUserRepository;
import com.yapai.dreamcatcher.service.IDreamService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DreamService implements IDreamService {

    private final IDreamRepository dreamRepository;
    private final IUserRepository userRepository;

    public DreamService(IDreamRepository dreamRepository, IUserRepository userRepository) {
        this.dreamRepository = dreamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DreamDto addDream(Authentication authentication, CreateDreamRequest createDreamRequest) {
        if (authentication == null) throw new AuthenticationCredentialsNotFoundException("User not authenticated");

        Dream dream = new Dream();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new RuntimeException("User not found"));

        dream.setDream(createDreamRequest.getDream());
        dream.setDreamInterpretation(createDreamRequest.getDreamInterpretation());
        dream.setUser(user);
        Dream savedDream = dreamRepository.save(dream);
        return DreamMapper.mapToDreamDto(savedDream);
    }

    @Override
    public void deleteDream(Authentication authentication, Long dreamId) {
        if (authentication == null) throw new AuthenticationCredentialsNotFoundException("User not authenticated");

        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(()-> new RuntimeException("Dream not found"));

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (dream.getUser().getId().equals(user.getId())) {
            dreamRepository.deleteById(dreamId);
        }
    }

    @Override
    public List<DreamDto> getAllDreams() {
        List<Dream> dreams = dreamRepository.findAll();
        List<DreamDto> dreamDtos = new ArrayList<>();
        if (!dreams.isEmpty()) {
            dreams.forEach((dream)-> dreamDtos.add(DreamMapper.mapToDreamDto(dream)));
        }
        return dreamDtos;
    }

    @Override
    public List<DreamDto> getAllDreamsOfUser(Authentication authentication) {
        if (authentication == null) throw new AuthenticationCredentialsNotFoundException("User not authenticated");

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<Dream> dreams = dreamRepository.findByUser(user);
        List<DreamDto> dreamDtos = new ArrayList<>();
        if (!dreams.isEmpty()) {
            dreams.forEach((dream)-> dreamDtos.add(DreamMapper.mapToDreamDto(dream)));
        }
        return dreamDtos;
    }
}
