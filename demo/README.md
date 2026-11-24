# Board Game Buddy: Spring AI in Action (Chapter 1 & 2)

Tugas berdasarkan buku *Spring AI in Action* (Craig Walls).

- **Chapter 1**: Getting started with Spring AI (REST `/ask`, `ChatClient`, test dengan WireMock)
- **Chapter 2**: Evaluating generated responses (`RelevancyEvaluator`, `FactCheckingEvaluator`, self-evaluation dengan `@Retryable`)

## Prasyarat
- JDK 17+ dan Gradle (atau salin folder `src/` ke proyek hasil start.spring.io yang sudah memiliki Gradle wrapper)
- API key OpenAI: https://platform.openai.com/api-keys

## Konfigurasi API key (jangan di-commit!)
```bash
# macOS / Linux
export SPRING_AI_OPENAI_API_KEY=sk-xxxx
# Windows (cmd)
set SPRING_AI_OPENAI_API_KEY=sk-xxxx
# Windows (PowerShell)
$env:SPRING_AI_OPENAI_API_KEY="sk-xxxx"
```

## Menjalankan
```bash
gradle wrapper                 # sekali saja, jika belum ada gradlew
./gradlew bootRun              # Chapter 1: SpringAiBoardGameService
./gradlew bootRun --args='--spring.profiles.active=evaluating'   # Chapter 2: SelfEvaluatingBoardGameService
```
Uji endpoint:
```bash
curl localhost:8080/ask -H "Content-type: application/json" -d '{"question":"Why is the sky blue?"}'
```

## Test
```bash
./gradlew test --tests '*WireMockTests'     # Ch.1, tanpa API key asli
./gradlew test --tests '*ServiceTests'      # Ch.2, memanggil OpenAI sungguhan
```

## Struktur & UML
Lihat `docs/uml/` (`chapter1-class-diagram.png`, `chapter2-class-diagram.png`).

## Catatan desain
`SpringAiBoardGameService` aktif secara default (`@Profile("!evaluating")`) dan `SelfEvaluatingBoardGameService` aktif pada profile `evaluating`, sehingga tidak terjadi bean ganda untuk `BoardGameService`.
