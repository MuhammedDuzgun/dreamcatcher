package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.model.DreamInterpretation;
import com.yapai.dreamcatcher.service.ai.IDreamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    private final IDreamService dreamService;

    public DreamController(IDreamService dreamService) {
        this.dreamService = dreamService;
    }

    @GetMapping
    public DreamInterpretation getDreamInterpretation(@RequestParam String dream) {
        return dreamService.getDreamInterpretation(dream);
    }

}
