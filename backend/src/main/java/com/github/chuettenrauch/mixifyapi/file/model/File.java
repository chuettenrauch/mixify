package com.github.chuettenrauch.mixifyapi.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private String id;
    private String fileName;
    private String contentType;
    private long size;

    private String url;

    @JsonIgnore
    private InputStream content;

    public File(String id, String fileName, String contentType, long size) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.url = String.format("/api/files/%s", this.id);
    }

    public static File create(GridFsResource gridFsResource) throws IOException {
        GridFSFile gridFSFile = gridFsResource.getGridFSFile();

        File file = gridFSFile != null ? create(gridFSFile) : new File();
        file.setContent(gridFsResource.getInputStream());

        return file;
    }

    public static File create(GridFSFile gridFSFile) {
        Document metadata = Optional
                .ofNullable(gridFSFile.getMetadata())
                .orElse(new Document(Map.of(
                        "_contentType", ""
                )));

        return new File(
                gridFSFile.getId().asObjectId().getValue().toString(),
                gridFSFile.getFilename(),
                metadata.get("_contentType").toString(),
                gridFSFile.getLength()
        );
    }
}
