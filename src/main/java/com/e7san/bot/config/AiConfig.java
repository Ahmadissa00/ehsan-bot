package com.e7san.bot.config;

import com.e7san.bot.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class AiConfig {

    private final ImageRepository imageRepository;

    public AiConfig(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) throws Exception {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        File vectorStoreFile = new File("src/main/resources/data/vectorstore.json");



        if (vectorStoreFile.exists()) {
            // تحميل الفكتورات من الملف إذا موجود
            log.info("Loading vector store from {}", vectorStoreFile.getAbsolutePath());
            vectorStore.load(vectorStoreFile);
        } else {
            // قراءة أسماء الصور من DB
            List<String> imageNames = imageRepository.findAll()
                    .stream()
                    .map(image -> image.getName())
                    .collect(Collectors.toList());

            File imageNamesFile = new File("src/main/resources/data/imagenames.txt");
            if (!imageNamesFile.exists()) {
                imageNamesFile.createNewFile();
                java.nio.file.Files.write(imageNamesFile.toPath(), imageNames);
            }

            TextReader reader = new TextReader("classpath:/data/imagenames.txt");

            // إضافة كل النصوص إلى VectorStore بدون توليد embeddings
            vectorStore.add(reader.get());

            // حفظ VectorStore في ملف JSON
            vectorStore.save(vectorStoreFile);
        }

        return vectorStore;
    }
}
