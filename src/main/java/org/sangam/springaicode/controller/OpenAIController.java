package org.sangam.springaicode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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

    public OpenAIController(ChatClient.Builder builder) {
       // this.openAiChatModel = openAiChatModel;
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)  // Only remember last 10 messages (excluding system)
                .build();


        MessageChatMemoryAdvisor advisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .conversationId("my-session")  // Optional: set default session id
                .build();

        this.chatClient = builder
                .defaultAdvisors(advisor)
                .build();
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
