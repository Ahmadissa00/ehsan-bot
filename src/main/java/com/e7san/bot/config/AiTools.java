package com.e7san.bot.config;

import com.e7san.bot.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AiTools {

    private static final Logger log = LoggerFactory.getLogger(AiTools.class);
    @Autowired
    private ImageRepository imageRepository;

//    public AiTools(ImageRepository imageRepository) {
//        this.imageRepository = imageRepository;
//    }

    @Tool(description = "Return link to image with given name and without the {name} you can return all photos links")
    public String returnLinks() {
        return "http://localhost:8080/api/v1/images";
    }


    @Tool(description = "Return all photos names in the database")
    public String photosNames() {

        String collect = imageRepository.findAll().stream()
                .map(image -> image.getName())
                .collect(Collectors.joining(", "));

        log.info("Return all photos names in the database: {}", collect);

        return collect;
    }

}

