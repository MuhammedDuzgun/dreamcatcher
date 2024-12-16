package com.yapai.dreamcatcher.service;

import com.yapai.dreamcatcher.dto.CreateDreamRequest;
import com.yapai.dreamcatcher.dto.DreamDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IDreamService {
    DreamDto addDream(Authentication authentication, CreateDreamRequest createDreamRequest);
    void deleteDream(Authentication authentication, Long dreamId);
    List<DreamDto> getAllDreams();
}
