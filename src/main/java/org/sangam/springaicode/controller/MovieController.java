package org.sangam.springaicode.controller;

import org.sangam.springaicode.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    private ChatClient chatClient;

    public MovieController(OpenAiChatModel openAiChatModel) {
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    @GetMapping("/movies")
    public List<String> getMovies(@RequestParam String name) {
    List<String> movies = chatClient.prompt().user(u->u.text("List p 5 companies of {name}").param("name",name))
            .call().entity(new ListOutputConverter(new DefaultConversionService()));
    return movies;

    }

    @GetMapping("/movie")
    public Movie getMovie(@RequestParam String name) {
        BeanOutputConverter<Movie> beanOutputConverter = new BeanOutputConverter<>(Movie.class);

        Movie movie = chatClient.prompt().user(u->u.text("List p 5 companies of {name}").param("name",name))
                .call().entity(new BeanOutputConverter<Movie>(Movie.class));
        return movie;
    }

    @GetMapping("/movieList")
    public List<Movie> getMovieList(@RequestParam String name) {
        BeanOutputConverter<Movie> beanOutputConverter = new BeanOutputConverter<>(Movie.class);

        List<Movie> movies = chatClient.prompt().user(u->u.text("List p 5 companies of {name}").param("name",name))
                .call().entity(new BeanOutputConverter<List<Movie>>(new ParameterizedTypeReference<List<Movie>>() {
                }));
        return movies;
    }


}
