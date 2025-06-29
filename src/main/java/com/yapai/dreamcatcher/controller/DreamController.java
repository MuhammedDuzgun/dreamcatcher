package com.yapai.dreamcatcher.controller;

import com.yapai.dreamcatcher.dto.CommentDto;
import com.yapai.dreamcatcher.request.CreateDreamRequest;
import com.yapai.dreamcatcher.dto.DreamDto;
import com.yapai.dreamcatcher.request.GetDreamInterpretationRequest;
import com.yapai.dreamcatcher.model.DreamInterpretation;
import com.yapai.dreamcatcher.service.ai.DreamServiceAI;
import com.yapai.dreamcatcher.service.crud.CommentService;
import com.yapai.dreamcatcher.service.crud.DreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    private final DreamServiceAI dreamServiceAI;
    private final DreamService dreamService;
    private final CommentService commentService;

    public DreamController(DreamServiceAI dreamServiceAI,
                           DreamService dreamService,
                           CommentService commentService) {
        this.dreamServiceAI = dreamServiceAI;
        this.dreamService = dreamService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<DreamInterpretation> getDreamInterpretation
            (@RequestBody GetDreamInterpretationRequest request) {
        DreamInterpretation dreamInterpretation = dreamServiceAI.getDreamInterpretation(request.dream());
        return ResponseEntity.ok(dreamInterpretation);
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

    @GetMapping("/all-dreams")
    public ResponseEntity<List<DreamDto>> getAllDreams() {
        return ResponseEntity.ok(dreamService.getAllDreams());
    }

    @GetMapping("/{dreamId}/all-comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByDreamId(@PathVariable("dreamId") Long dreamId) {
        List<CommentDto> commentDtos = commentService.getAllCommentsByDreamId(dreamId);
        return ResponseEntity.ok(commentDtos);
    }
}
