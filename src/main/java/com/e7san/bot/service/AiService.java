package com.e7san.bot.service;

import com.e7san.bot.entity.HistoryMessage;
import com.e7san.bot.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {


    private HistoryMessage historyMessage;
    private ChatClient chatClient;

    public AiService(ChatClient.Builder builder , HistoryMessage historyMessage) {
        this.chatClient = builder
                .build();
        this.historyMessage = historyMessage;
    }

    public String askQuestion(String question) {


        var history = historyMessage.getAllMessages().stream()
                .map(m -> {
                    if ("user".equalsIgnoreCase(m.getRole())) {
                        return new UserMessage(m.getContent());
                    } else {
                        return new AssistantMessage(m.getContent());
                    }
                })
                .toList();

        log.info("History: {}", history);


        var query = Query.builder()
                .text(question)
                .history(history.toArray(new org.springframework.ai.chat.messages.Message[0]))
                .build();

        log.info("Query: {}", query);


        var queryTransformer = CompressionQueryTransformer.builder()
                .chatClientBuilder(chatClient.mutate())
                .build();

        log.info("QueryTransformer: {}", queryTransformer.toString());
        var transformedQuery = queryTransformer.transform(query);

        log.info("TransformedQuery: {}", transformedQuery);




        historyMessage.saveMessage("user", question);
        historyMessage.saveMessage("assistant", transformedQuery.text());

        return transformedQuery.text();

    }

}
