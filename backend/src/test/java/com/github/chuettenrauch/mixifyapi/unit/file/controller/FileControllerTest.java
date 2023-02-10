package com.github.chuettenrauch.mixifyapi.unit.file.controller;

import com.github.chuettenrauch.mixifyapi.file.controller.FileController;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.security.config.SecurityConfig;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
@ImportAutoConfiguration(classes = SecurityConfig.class)
class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private UserService userService;

    @Test
    void uploadFile_whenNotLoggedIn_returnUnauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "some image".getBytes());

        this.mvc.perform(multipart("/api/files")
                        .file(file)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void downloadFile_whenNotLoggedIn_returnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/files/123"))
                .andExpect(status().isUnauthorized());
    }

}
