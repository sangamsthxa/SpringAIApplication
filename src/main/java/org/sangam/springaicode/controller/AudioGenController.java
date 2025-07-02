package org.sangam.springaicode.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioGenController {

    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AudioGenController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }


    @PostMapping("/api/stt")
    public String speechToText(@RequestParam MultipartFile file) {

        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .language("np")
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT)
                .build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(), options);

        return openAiAudioTranscriptionModel.call(prompt).getResult().getOutput();

    }

    @PostMapping("/api/tts")
    public byte[] TextToSpeech(@RequestParam String text) {

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .speed(1.25f)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .build();

        SpeechPrompt prompt = new SpeechPrompt(text, options);

        return openAiAudioSpeechModel.call(prompt).getResult().getOutput();

    }



}
