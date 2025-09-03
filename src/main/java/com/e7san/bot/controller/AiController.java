package com.e7san.bot.controller;

import ch.qos.logback.classic.Logger;
import com.e7san.bot.config.AiTools;
import com.e7san.bot.entity.HistoryMessage;
import com.e7san.bot.repository.ImageRepository;
import com.e7san.bot.service.AiService;
import com.e7san.bot.service.ImageService;
import com.google.cloud.vertexai.api.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.rag.Query;

import java.util.Map;

@RestController
@Slf4j
public class AiController {


    private ChatClient chatClient;
    private VectorStore vectorStore;
    private AiService aiService;
    private AiTools aiTools;


    public  AiController(ChatClient.Builder builder, VectorStore vectorStore, AiTools aiTools,ChatMemory chatMemory,AiService aiService) {
        this.aiTools = aiTools;
        this.vectorStore = vectorStore;
        this.aiService = aiService;
        this.chatClient = builder
                .defaultTools(aiTools)
                .defaultAdvisors( new QuestionAnswerAdvisor(vectorStore))
                .build();

    }

    @GetMapping("/ai")
    public String tryEmbedding(@RequestParam String ask) {
        String content = chatClient.prompt()
                .system("when i ask about photo with name or i give you a word or sentence" +
                        "if you get asked about anything else check the tools if not found response with sorry i can't help " +
                        " look like a photo name response with http://localhost:8080/api/v1/images/{name}.png ")
                .user(aiService.askQuestion(ask))
                .call()
                .content();
        log.info("content: {}", content);

        return content;
    }
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String aiReply = tryEmbedding(userMessage);
        return Map.of("reply", aiReply);
    }








}
