package com.samplesecurity.controller;

import com.samplesecurity.domain.board.UploadFile;
import com.samplesecurity.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ResourceLoader resourceLoader;

    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
        try {
            UploadFile uploadFile = imageService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadFile.getId());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> display(@PathVariable("id") Long id){
        try {
            UploadFile uploadFile = imageService.load(id);
            log.info("uploadFile: "+uploadFile);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            log.info("resource: "+resource);
            return ResponseEntity.ok().body(resource);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
