package com.github.chuettenrauch.mixifyapi.file.service;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    File saveFileForUser(MultipartFile file, User user) throws IOException;

    File findFileByIdForUser(String id, User user) throws IOException;
}
