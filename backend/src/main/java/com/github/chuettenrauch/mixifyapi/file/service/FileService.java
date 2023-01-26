package com.github.chuettenrauch.mixifyapi.file.service;

import com.github.chuettenrauch.mixifyapi.file.exception.FileNotFoundException;
import com.github.chuettenrauch.mixifyapi.file.exception.FileOperationUnauthorizedException;
import com.github.chuettenrauch.mixifyapi.file.exception.InvalidFileException;
import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import com.github.chuettenrauch.mixifyapi.user.service.UserService;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GridFsOperations gridFsOperations;

    private final UserService userService;

    public File saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new InvalidFileException();
        }

        User authenticatedUser = this.userService.getAuthenticatedUser().orElseThrow(FileOperationUnauthorizedException::new);

        DBObject metadata = BasicDBObjectBuilder
                .start()
                .add("createdBy", authenticatedUser.getId())
                .get();

        ObjectId fileId = this.gridFsOperations.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metadata
        );

        return File.create(this.findGridFSFileById(fileId.toString()));
    }

    public File findFileById(String id) throws IOException {
        GridFsResource gridFsResource = this.gridFsOperations.getResource(this.findGridFSFileById(id));

        return File.create(gridFsResource);
    }

    private GridFSFile findGridFSFileById(String id) {
        User authenticatedUser = this.userService.getAuthenticatedUser().orElseThrow(FileOperationUnauthorizedException::new);

        Query query = new Query().addCriteria(Criteria
                        .where("_id").is(id)
                        .and("metadata.createdBy").is(authenticatedUser.getId())
                );

        GridFSFile gridFSFile = this.gridFsOperations.findOne(query);

        return Optional.ofNullable(gridFSFile).orElseThrow(FileNotFoundException::new);
    }
}
