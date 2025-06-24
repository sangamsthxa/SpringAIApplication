package org.sangam.springaicode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {

    private OpenAiChatModel openAiChatModel;
    private ChatClient chatClient;

    public OpenAIController(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    @GetMapping("/api/{message}")
    public String getAnswer(@PathVariable String message){

        String response = openAiChatModel.call(message);
        return response;
    }

    @GetMapping("/api/chatClient/{message}")
    public ResponseEntity<String> getChatClientAnswer(@PathVariable String message){

        String response = chatClient.prompt(message).call().content();
        ChatResponse chatResponse = chatClient.prompt(message).call().chatResponse();
        System.out.println(chatResponse.getMetadata().getModel());
        response = chatResponse
                .getResult()
                .getOutput()
                .getText();

        return ResponseEntity.ok(response);
    }



}
