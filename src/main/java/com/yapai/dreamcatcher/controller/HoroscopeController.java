package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.GetHoroscopeInterpretationRequest;
import com.yapai.dreamcatcher.model.HoroscopeInterpretation;
import com.yapai.dreamcatcher.service.ai.HoroscopeServiceAI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horoscopes")
public class HoroscopeController {

    private final HoroscopeServiceAI horoscopeServiceAI;

    public HoroscopeController(HoroscopeServiceAI horoscopeServiceAI) {
        this.horoscopeServiceAI = horoscopeServiceAI;
    }

    @GetMapping
    public ResponseEntity<HoroscopeInterpretation> getHoroscopeInterpretation
            (@RequestBody GetHoroscopeInterpretationRequest request) {
        HoroscopeInterpretation horoscopeInterpretation =
                horoscopeServiceAI.getHoroscopeInterpretation(request.horoscope());
        return ResponseEntity.ok(horoscopeInterpretation);
    }
}