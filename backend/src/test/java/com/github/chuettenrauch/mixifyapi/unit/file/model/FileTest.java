package com.github.chuettenrauch.mixifyapi.unit.file.model;

import com.github.chuettenrauch.mixifyapi.file.model.File;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileTest {

    @Test
    void create_whenGridFSResourceWithoutGridFSFile_thenReturnFileWithOnlyContent() throws IOException {
        InputStream content = mock(InputStream.class);

        GridFsResource gridFsResource = mock(GridFsResource.class);
        when(gridFsResource.getInputStream()).thenReturn(content);

        File actual = File.create(gridFsResource);

        assertEquals(content, actual.getContent());
        assertNull(actual.getId());
        assertNull(actual.getFileName());
        assertNull(actual.getContentType());
        assertEquals(0, actual.getSize());
    }
}
