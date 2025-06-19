package org.sangam.springaicode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {

    @GetMapping("/api/{message}")
    public String getAnswer(@PathVariable String message){
        return "Hello " + message;
    }

}
