package com.e7san.bot.service;

import com.e7san.bot.entity.Image;
import com.e7san.bot.repository.ImageRepository;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }




    public List<String> getAllImageLinks() {
        return imageRepository.findAll().stream()
                .map(img -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/images/")
                        .path(img.getName())
                        .toUriString())
                .collect(Collectors.toList());
    }


    public String uploadImage(MultipartFile file, String imageName) {
        Image image = new Image();
        if (imageRepository.existsByName(imageName)) {
            return "Failed to upload: Image name already exists!";
        }
        try {
            imageType(file, image);
            image.setName(imageName);
            image.setData(file.getBytes());
            image.setContentType(file.getContentType());
            imageRepository.save(image);
            return "Image uploaded successfully: " + file.getOriginalFilename();
        } catch (Exception e) {
            return "Failed to upload image: " + e.getMessage();
        }
    }

    private static void imageType(MultipartFile file, Image image) {
        String contentType = file.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            // fallback حسب امتداد الملف
            String filename = file.getOriginalFilename();
            if (filename != null) {
                if (filename.endsWith(".png")) contentType = "image/png";
                else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) contentType = "image/jpeg";
                else if (filename.endsWith(".gif")) contentType = "image/gif";
                else contentType = "application/octet-stream"; // نوع عام
            }
        }

        image.setContentType(contentType);
    }

    public ResponseEntity<byte[]> getImageByName(String name) {
        Image image = imageRepository.findByName(name);
        if (image != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, image.getContentType() != null ? image.getContentType() : "application/octet-stream")
                    .body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
