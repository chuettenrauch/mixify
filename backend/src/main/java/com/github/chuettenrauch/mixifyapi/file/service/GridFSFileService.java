package com.github.chuettenrauch.mixifyapi.file.service;

import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.BadRequestException;
import com.github.chuettenrauch.mixifyapi.file.model.File;
import com.github.chuettenrauch.mixifyapi.user.model.User;
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
public class GridFSFileService implements FileService {

    private final GridFsOperations gridFsOperations;

    public File saveFileForUser(MultipartFile file, User user) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException();
        }

        DBObject metadata = BasicDBObjectBuilder
                .start()
                .add("createdBy", user.getId())
                .get();

        ObjectId fileId = this.gridFsOperations.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metadata
        );

        GridFSFile gridFSFile = this.findGridFSFileByIdAndCreatedBy(fileId.toString(), user.getId());

        return File.create(gridFSFile);
    }

    public File findFileByIdForUser(String id, User user) throws IOException {
        GridFsResource gridFsResource = this.gridFsOperations.getResource(
                this.findGridFSFileByIdAndCreatedBy(id, user.getId())
        );

        return File.create(gridFsResource);
    }

    private GridFSFile findGridFSFileByIdAndCreatedBy(String id, String createdBy) {
        Query query = new Query().addCriteria(Criteria
                        .where("_id").is(id)
                        .and("metadata.createdBy").is(createdBy)
                );

        GridFSFile gridFSFile = this.gridFsOperations.findOne(query);

        return Optional.ofNullable(gridFSFile).orElseThrow(NotFoundException::new);
    }
}
