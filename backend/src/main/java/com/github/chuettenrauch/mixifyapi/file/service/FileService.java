package com.github.chuettenrauch.mixifyapi.file.service;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    File saveFile(MultipartFile file) throws IOException;

    File findFileById(String id) throws IOException;
}
