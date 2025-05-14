package com.idpr.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileDownloadUtil {

    public Resource getFileAsResource(Long userId, String fileName) throws IOException {
        Path filePath = Paths.get("uploads", userId.toString(), fileName); // Directly create path

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            return new UrlResource(filePath.toUri()); // Return resource if file exists
        }

        return null; // Return null if file not found
    }
}
