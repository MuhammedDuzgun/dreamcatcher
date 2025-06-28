package com.yapai.dreamcatcher.service.ai;

import com.yapai.dreamcatcher.model.HoroscopeInterpretation;
import com.yapai.dreamcatcher.utils.AppConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class HoroscopeServiceAI {

    private final ChatClient chatClient;

    public HoroscopeServiceAI(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(AppConstants.HOROSCOPE_PROMPT)
                .build();
    }

    public HoroscopeInterpretation getHoroscopeInterpretation(String horoscope) {
        return chatClient.prompt()
                .user(user -> user.text(horoscope + "burcu için bugünün yorumunu yapar mısın ?"))
                .call()
                .entity(HoroscopeInterpretation.class);
    }
}