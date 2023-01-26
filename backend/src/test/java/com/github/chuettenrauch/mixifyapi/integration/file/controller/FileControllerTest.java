package com.github.chuettenrauch.mixifyapi.integration.file.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void uploadFile_whenNotLoggedIn_returnUnauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "some image".getBytes());

        this.mvc.perform(multipart("/api/files")
                        .file(file)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void uploadFile_whenLoggedInButUserDoesNotExistInDB_returnUnauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());

        this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void uploadFile_whenLoggedInButFileIsEmpty_returnBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "".getBytes());

        this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void uploadFile_whenLoggedIn_returnFileMetadata() throws Exception {
        // given
        User user = new User("123", "user", "alvin", "/path/to/image", Provider.spotify, "user-123");
        this.userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());

        String expectedJson = String.format("""
                {
                    "fileName": "%s",
                    "contentType": "%s",
                    "size": %d,
                    "createdBy" : "%s"
                }
                """, file.getOriginalFilename(), file.getContentType(), file.getSize(), user.getId());

        // when + then
        this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void downloadFile_whenNotLoggedIn_returnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/files/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void downloadFile_whenLoggedInButUserDoesNotExistInDB_returnUnauthorized() throws Exception {
        this.mvc.perform(get("/api/files/123")
                        .with(oauth2Login())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void downloadFile_whenLoggedInButFileDoesNotExist_returnNotFound() throws Exception {
        // given
        User user = new User("123", "user", "alvin", "/path/to/image", Provider.spotify, "user-123");
        this.userRepository.save(user);

        this.mvc.perform(get("/api/files/123")
                        .with(oauth2Login())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void downloadFile_whenLoggedIn_returnFile() throws Exception {
        // given
        User user = new User("123", "user", "alvin", "/path/to/image", Provider.spotify, "user-123");
        this.userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());

        String postResult = this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        File uploadedFile = objectMapper.readValue(postResult, File.class);

        this.mvc.perform(get("/api/files/" + uploadedFile.getId())
                        .with(oauth2Login())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"));
    }
}
