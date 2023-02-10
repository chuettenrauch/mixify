package com.github.chuettenrauch.mixifyapi.file.service;

import com.github.chuettenrauch.mixifyapi.exception.NotFoundException;
import com.github.chuettenrauch.mixifyapi.exception.BadRequestException;
import com.github.chuettenrauch.mixifyapi.file.model.File;
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

    public File saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException();
        }

        ObjectId fileId = this.gridFsOperations.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        GridFSFile gridFSFile = this.findGridFSFileById(fileId.toString());

        return File.create(gridFSFile);
    }

    public File findFileById(String id) throws IOException {
        GridFsResource gridFsResource = this.gridFsOperations.getResource(
                this.findGridFSFileById(id)
        );

        return File.create(gridFsResource);
    }

    private GridFSFile findGridFSFileById(String id) {
        Query query = new Query().addCriteria(Criteria
                        .where("_id").is(id)
                );

        GridFSFile gridFSFile = this.gridFsOperations.findOne(query);

        return Optional.ofNullable(gridFSFile).orElseThrow(NotFoundException::new);
    }
}
