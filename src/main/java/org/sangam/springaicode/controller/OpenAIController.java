package org.sangam.springaicode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OpenAIController {

   // private OpenAiChatModel openAiChatModel;
    private ChatClient chatClient;

    @Autowired
    private EmbeddingModel embeddingModel;


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

        //String response = openAiChatModel.call(message);
        return message;
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

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type, @RequestParam String year, @RequestParam String lang){
        String tempt = """
                I want to watch a {type} movie tonight with good rating,
                looking for movies around this year {year}.
                The language im looking for is {lang}.
                Suggest one specific movie and tell me the cast and length of the movie.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(tempt);
        Prompt prompt = promptTemplate.create(Map.of("type", type, "year", year, "lang", lang));

        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return response;
    }

    @PostMapping("/api/embedding")
    public float[] embedding(@RequestParam String text){
        return embeddingModel.embed(text);
    }

    @PostMapping("/api/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2){

        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);

        double dotProduct =0;
        double norm1 =0;
        double norm2 =0;

        for(int i=0;i<embedding1.length;i++){
            dotProduct += embedding1[i]*embedding2[i];
            norm1 += Math.pow(embedding1[i],2);
            norm2 += Math.pow(embedding2[i],2);
        }

        return dotProduct / Math.sqrt(norm1) * Math.sqrt(norm2);
    }



}
