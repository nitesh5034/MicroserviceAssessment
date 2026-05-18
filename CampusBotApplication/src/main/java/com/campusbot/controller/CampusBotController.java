package com.campusbot.controller;

import com.campusbot.dto.QueryReuest;
import com.campusbot.dto.botresponse;
import com.campusbot.service.CampusBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CampusBotController {

    @Autowired
    private CampusBotService botService;

    @PostMapping(value = {"/ask", "/ask-question"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<botresponse> handleCampusQuery(@RequestBody QueryReuest request) throws IOException, InterruptedException {
      
        String aiAnswer = botService.fetchAIResponse(request.getQuery());
        
        botresponse successResponse = new botresponse(aiAnswer, "SUCCESS");
        return ResponseEntity.ok(successResponse);
    }
}