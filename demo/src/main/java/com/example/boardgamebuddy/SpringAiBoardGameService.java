package com.example.boardgamebuddy;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Chapter 1: implementasi BoardGameService memakai Spring AI ChatClient.
 * Aktif secara default. Pada profile "evaluating" digantikan oleh
 * SelfEvaluatingBoardGameService (Chapter 2) supaya tidak ada dua bean ambigu.
 */
@Service
@Profile("!evaluating")
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(question.question())
                .call()
                .content();
        return new Answer(answerText);
    }
}
