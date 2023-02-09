package com.github.chuettenrauch.mixifyapi.integration.file.controller;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.file.service.FileService;
import com.github.chuettenrauch.mixifyapi.user.model.Provider;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

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
        // given
        User user = new User("123", "user", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        this.userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "".getBytes());

        this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFile_whenLoggedIn_returnFileMetadata() throws Exception {
        // given
        User user = new User("123", "user", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        this.userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());

        String expectedJson = """
                {
                    "fileName": "file.txt",
                    "contentType": "text/plain",
                    "size": 10,
                    "createdBy" : "123"
                }
                """;

        // when + then
        this.mvc.perform(multipart("/api/files")
                        .file(file)
                        .with(oauth2Login())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.url", matchesPattern("/api/files/[\\w]+")));
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
    void downloadFile_whenLoggedInButFileDoesNotExist_returnNotFound() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        this.userRepository.save(user);

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", user.getEmail()
        ), "email");

        // when + then
        this.mvc.perform(get("/api/files/123")
                .with(oauth2Login().oauth2User(oAuth2User))
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void downloadFile_whenLoggedInButFileDoesNotBelongsToLoggedInUser_returnNotFound() throws Exception {
        // given
        User fileCreator = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        User loggedInUser = new User("234", "simon@chipmunks.de", "simon", "/path/to/image", Provider.SPOTIFY, "user-234");
        this.userRepository.saveAll(List.of(fileCreator, loggedInUser));

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, fileCreator);

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
            "email", loggedInUser.getEmail()
        ), "email");

        // when + then
        this.mvc.perform(get("/api/files/" +  uploadedFile.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadFile_whenLoggedIn_returnFile() throws Exception {
        // given
        User user = new User("123", "alvin@chipmunks.de", "alvin", "/path/to/image", Provider.SPOTIFY, "user-123");
        this.userRepository.save(user);

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Map.of(
                "email", user.getEmail()
        ), "email");

        MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "some image".getBytes());
        File uploadedFile = this.fileService.saveFileForUser(file, user);

        this.mvc.perform(get("/api/files/" + uploadedFile.getId())
                        .with(oauth2Login().oauth2User(oAuth2User))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"));
    }

}
