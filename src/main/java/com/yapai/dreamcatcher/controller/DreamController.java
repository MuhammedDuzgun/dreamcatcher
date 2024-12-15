package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.CreateDreamRequest;
import com.yapai.dreamcatcher.dto.DreamDto;
import com.yapai.dreamcatcher.model.DreamInterpretation;
import com.yapai.dreamcatcher.service.IDreamService;
import com.yapai.dreamcatcher.service.ai.IDreamServiceAI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    private final IDreamServiceAI dreamServiceAI;
    private final IDreamService dreamService;

    public DreamController(IDreamServiceAI dreamService, IDreamService dreamService1) {
        this.dreamServiceAI = dreamService;
        this.dreamService = dreamService1;
    }

    @GetMapping
    public DreamInterpretation getDreamInterpretation(@RequestParam String dream) {
        return dreamServiceAI.getDreamInterpretation(dream);
    }

    @PostMapping("/add-dream")
    public ResponseEntity<DreamDto> addDream(Authentication authentication,
                                             @RequestBody CreateDreamRequest createDreamRequest) {
        DreamDto dreamDto = dreamService.addDream(authentication, createDreamRequest);
        return ResponseEntity.ok(dreamDto);
    }

    @DeleteMapping("/delete-dream/{dreamId}")
    public ResponseEntity<String> deleteDream(Authentication authentication,
                                              @PathVariable("dreamId") Long dreamId){
        dreamService.deleteDream(authentication, dreamId);
        return ResponseEntity.ok("Deleted Dream");
    }

}
