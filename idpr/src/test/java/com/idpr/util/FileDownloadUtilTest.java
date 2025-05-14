package com.idpr.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FileDownloadUtilTest {

    private FileDownloadUtil fileDownloadUtil;

    @BeforeEach
    void setUp() {
        fileDownloadUtil = new FileDownloadUtil();
    }

    @Test
    void getFileAsResource_withExistingFile_returnsResource() throws IOException {
        Path mockPath = Paths.get("uploads", "1", "test.txt");
        Files.createDirectories(mockPath.getParent());
        Files.createFile(mockPath);

        try {
            Resource resource = fileDownloadUtil.getFileAsResource(1L, "test.txt");
            assertNotNull(resource);
            assertTrue(resource.exists());
        } finally {
            Files.deleteIfExists(mockPath);
            Files.deleteIfExists(mockPath.getParent());
        }
    }

    @Test
    void getFileAsResource_withNonExistentFile_returnsNull() throws IOException {
        Resource resource = fileDownloadUtil.getFileAsResource(1L, "nonexistent.txt");
        assertNull(resource);
    }

    @Test
    void getFileAsResource_withDirectoryInsteadOfFile_returnsNull() throws IOException {
        Path mockPath = Paths.get("uploads", "1", "directory");
        Files.createDirectories(mockPath);

        try {
            Resource resource = fileDownloadUtil.getFileAsResource(1L, "directory");
            assertNull(resource);
        } finally {
            Files.deleteIfExists(mockPath);
            Files.deleteIfExists(mockPath.getParent());
        }
    }

    @Test
    void getFileAsResource_withInvalidPath_throwsIOException() {
        assertThrows(InvalidPathException.class, () -> fileDownloadUtil.getFileAsResource(1L, "\0invalid.txt"));
    }
}