package com.github.chuettenrauch.mixifyapi.file.controller;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public File uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return this.fileService.saveFile(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) throws IOException {
        File file = this.fileService.findFileById(id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(file.getContentType()))
                .body(new InputStreamResource(file.getContent()));
    }

}
