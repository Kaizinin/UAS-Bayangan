# Board Game Buddy: Spring AI in Action (Chapter 1 & 2)

Tugas mata kuliah **Rekayasa Perangkat Lunak Lanjut**, berdasarkan buku *Spring AI in Action* oleh Craig Walls.

| | |
|---|---|
| Nama | Ryan Fazli Mawla |
| NIM | 12409011010016 |
| Dosen | rizalbroer@uinjkt.ac.id |

## Deskripsi

Aplikasi REST sederhana yang menerima pertanyaan, mengirimkannya ke LLM OpenAI melalui **Spring AI**, lalu mengembalikan jawabannya.

- **Chapter 1: Getting Started with Spring AI**: endpoint `POST /ask`, `ChatClient`, dan test deterministik dengan WireMock.
- **Chapter 2: Evaluating Generated Responses**: `RelevancyEvaluator`, `FactCheckingEvaluator`, dan self-evaluation saat runtime dengan `@Retryable` dan `@Recover`.

## Teknologi

- Java 17+
- Spring Boot 3.5.5
- Spring AI 1.0.1 (OpenAI, model default `gpt-4o-mini`)
- Gradle (Groovy DSL)
- JUnit 5, AssertJ, WireMock

## Prasyarat

1. **JDK 17 atau lebih baru**. Cek dengan `java -version`.
2. **API key OpenAI** dari https://platform.openai.com/api-keys. Akun harus memiliki saldo (billing), kalau tidak akan muncul error `429 insufficient_quota`.
3. Koneksi internet (untuk mengunduh dependency dan memanggil OpenAI).

Tidak perlu menginstal Gradle, karena proyek memakai Gradle wrapper (`gradlew`).

## Konfigurasi API key

API key **tidak boleh** ditulis di kode atau di-commit. Simpan di environment variable `SPRING_AI_OPENAI_API_KEY`, di terminal yang sama dengan tempat aplikasi dijalankan.

```powershell
# Windows PowerShell
$env:SPRING_AI_OPENAI_API_KEY="sk-xxxx"

# Windows cmd
set SPRING_AI_OPENAI_API_KEY=sk-xxxx
```

```bash
# macOS / Linux
export SPRING_AI_OPENAI_API_KEY=sk-xxxx
```

Opsional, mengganti model di `src/main/resources/application.properties`:

```properties
spring.ai.openai.chat.options.model=gpt-4.1-nano
```

## Menjalankan aplikasi

### Chapter 1: layanan dasar

```powershell
.\gradlew.bat bootRun        # Windows
./gradlew bootRun            # macOS / Linux
```

Run pertama agak lama karena mengunduh dependency. Aplikasi siap saat muncul `Started BoardGameBuddyApplication`. Selama server berjalan, terminal akan terlihat "menggantung" (`EXECUTING`); itu normal.

### Chapter 2: layanan dengan self-evaluation

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=evaluating"
```

Pada profile ini jawaban dievaluasi relevansinya dan diulang (maksimal 3 kali) bila tidak relevan.

## Mencoba endpoint

Buka terminal **kedua** saat aplikasi berjalan.

```powershell
# Windows PowerShell
Invoke-RestMethod -Uri http://localhost:8080/ask -Method Post -ContentType "application/json" -Body '{"question":"Why is the sky blue?"}'
```

```bash
# macOS / Linux / Git Bash
curl localhost:8080/ask -H "Content-type: application/json" -d '{"question":"Why is the sky blue?"}'
```

Contoh respons:

```json
{"answer":"The sky appears blue because of Rayleigh scattering ..."}
```

Jawaban akan berbeda pada setiap pemanggilan karena LLM bersifat non-deterministik.

## Menjalankan test

```powershell
# Chapter 1: tidak membutuhkan API key asli (OpenAI ditiru dengan WireMock)
.\gradlew.bat test --tests "*WireMockTests"

# Chapter 2: memanggil OpenAI sungguhan, API key wajib di-set
.\gradlew.bat test --tests "SpringAiBoardGameServiceTests"
.\gradlew.bat test --tests "SelfEvaluatingBoardGameServiceTests"
```

## Struktur proyek

```
.
├── build.gradle
├── settings.gradle
├── docs/uml/                      # class diagram Chapter 1 & 2
└── src
    ├── main
    │   ├── java/com/example/boardgamebuddy
    │   │   ├── BoardGameBuddyApplication.java      # entry point, @EnableRetry
    │   │   ├── BoardGameService.java               # interface
    │   │   ├── SpringAiBoardGameService.java       # Chapter 1 (ChatClient)
    │   │   ├── SelfEvaluatingBoardGameService.java # Chapter 2 (evaluator + retry)
    │   │   ├── AnswerNotRelevantException.java
    │   │   ├── AskController.java                  # POST /ask
    │   │   ├── Question.java                       # record
    │   │   └── Answer.java                         # record
    │   └── resources/application.properties
    └── test
        ├── java/com/example/boardgamebuddy
        │   ├── SpringAiBoardGameServiceWireMockTests.java   # Chapter 1
        │   ├── SpringAiBoardGameServiceTests.java           # Chapter 2 (evaluator)
        │   └── SelfEvaluatingBoardGameServiceTests.java     # Chapter 2 (runtime)
        └── resources/test-openai-response.json
```

## Diagram UML

**Chapter 1**

![Class diagram Chapter 1](docs/uml/chapter1-class-diagram.png)

**Chapter 2**

![Class diagram Chapter 2](docs/uml/chapter2-class-diagram.png)

## Catatan desain

- `SpringAiBoardGameService` aktif secara default (`@Profile("!evaluating")`), sedangkan `SelfEvaluatingBoardGameService` aktif pada profile `evaluating`. Dengan begitu hanya ada satu bean `BoardGameService` pada satu waktu.
- `@EnableRetry` serta dependency `spring-retry` dan `spring-boot-starter-aop` diperlukan agar `@Retryable` dan `@Recover` berfungsi.
- Test WireMock memakai API key palsu sehingga dapat dijalankan tanpa biaya.

## Referensi

- Walls, C. *Spring AI in Action*. Manning Publications.
- Spring AI Reference: https://docs.spring.io/spring-ai/reference/
