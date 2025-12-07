package com.email.writer;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import com.email.writer.EmailGeneratorService;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")



public class EmailGeneratorController {


    private final EmailGeneratorService emailGeneratorService;
    @Autowired
    public EmailGeneratorController(EmailGeneratorService emailGeneratorService) {
        this.emailGeneratorService = emailGeneratorService;
    }
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
        String response =emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);

    }



}

