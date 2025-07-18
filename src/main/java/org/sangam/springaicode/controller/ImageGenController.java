package org.sangam.springaicode.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageGenController {

    private ChatClient chatClient;

    private OpenAiImageModel openAiImageModel;


    public ImageGenController(OpenAiImageModel openAiImageModel, OpenAiChatModel chatModel) {
        this.openAiImageModel = openAiImageModel;
        this.chatClient = ChatClient.create(chatModel);
    }

    //Generate Image
    @GetMapping("/image/{query}")
    public String getImage(@PathVariable String query) {

        ImagePrompt imagePrompt = new ImagePrompt(query);

        ImageResponse imageResponse = openAiImageModel.call(imagePrompt);
        return imageResponse.getResult().getOutput().getUrl();

    }

    //Generate Image with image options
    @GetMapping("/imageOptions/{query}")
    public String getImageWithOptions(@PathVariable String query) {

        ImagePrompt imagePrompt = new ImagePrompt(query, OpenAiImageOptions.builder()
                .quality("hd")
                .height(1024)
                .width(1024)
                .style("natural")
                .build());

        ImageResponse imageResponse = openAiImageModel.call(imagePrompt);
        return imageResponse.getResult().getOutput().getUrl();

    }

    @PostMapping("/image/describe")
    public String descImage(@RequestParam String query, @RequestParam MultipartFile file) {

        return chatClient.prompt()
                .user(us -> us.text(query)
                .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
                .call()
                .content();
    }

}
