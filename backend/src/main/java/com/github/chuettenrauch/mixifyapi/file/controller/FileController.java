package com.github.chuettenrauch.mixifyapi.file.controller;

import com.github.chuettenrauch.mixifyapi.exception.UnauthorizedException;
import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public File uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        User authenticatedUser = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        return this.fileService.saveFileForUser(file, authenticatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) throws IOException {
        User authenticatedUser = this.userService.getAuthenticatedUser()
                .orElseThrow(UnauthorizedException::new);

        File file = this.fileService.findFileByIdForUser(id, authenticatedUser);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(file.getContentType()))
                .body(new InputStreamResource(file.getContent()));
    }

}
