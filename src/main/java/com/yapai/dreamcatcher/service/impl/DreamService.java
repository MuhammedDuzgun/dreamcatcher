package com.yapai.dreamcatcher.service.impl;

import com.yapai.dreamcatcher.model.DreamInterpretation;
import com.yapai.dreamcatcher.service.IDreamService;
import com.yapai.dreamcatcher.utils.AppConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DreamService implements IDreamService {

    private final ChatClient chatClient;

    public DreamService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(AppConstants.DREAM_PROMPT)
                .build();
    }

    @Override
    public DreamInterpretation getDreamInterpretation(String dream) {
        return chatClient.prompt()
                .user(user -> user.text(dream + "Rüyası için tabir yapar mısın ?"))
                .call()
                .entity(DreamInterpretation.class);
    }

}
