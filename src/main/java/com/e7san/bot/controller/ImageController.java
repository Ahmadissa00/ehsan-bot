package com.e7san.bot.controller;

import com.e7san.bot.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ImageController {
    
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Tool(description = "Get all image links")
    @GetMapping("/images")
    public List<String> getAllImages() {
        return imageService.getAllImageLinks();
    }


    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,@RequestParam String imageName) {
        if (file.isEmpty()) {
            log.info("File is empty");
        }
        log.info("Uploading file: ");
        return imageService.uploadImage(file,imageName);
    }

    @GetMapping("/images/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable String name) {
        return imageService.getImageByName(name);
    }
}
