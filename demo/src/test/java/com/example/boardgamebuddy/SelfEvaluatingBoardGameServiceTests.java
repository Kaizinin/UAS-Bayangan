package com.example.boardgamebuddy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Chapter 2: menguji self-evaluation runtime (profile "evaluating"). Butuh API key asli. */
@SpringBootTest
@ActiveProfiles("evaluating")
public class SelfEvaluatingBoardGameServiceTests {

    @Autowired
    private BoardGameService boardGameService;

    @Test
    public void usesSelfEvaluatingImplementation() {
        Assertions.assertThat(boardGameService).isInstanceOf(SelfEvaluatingBoardGameService.class);
    }

    @Test
    public void returnsRelevantAnswerOrApology() {
        var answer = boardGameService.askQuestion(new Question("Why is the sky blue?"));
        Assertions.assertThat(answer.answer()).isNotBlank();
    }
}
