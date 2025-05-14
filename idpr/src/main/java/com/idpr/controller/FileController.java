package com.idpr.controller;

import com.idpr.model.FileMetadata;
import com.idpr.repository.FileMetadataRepository;
import com.idpr.service.FileEncryptionService;
import com.idpr.util.FileDownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FileEncryptionService encryptionService;
    private final FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();

    public FileController() {
        new File(UPLOAD_DIR).mkdirs();
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file,
                                             @RequestParam Long userId,
                                             @RequestParam String fileType) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty!");
            }

            // Check if a file of the same type exists for the user
            FileMetadata existingFile = fileMetadataRepository.findByUserIdAndFileType(userId, fileType);
            String encryptedFileName = file.getOriginalFilename() + ".enc";
            File userDir = new File(UPLOAD_DIR + userId);

            if (!userDir.exists()) userDir.mkdirs();

            // If a file of the same type exists, delete the old file
            if (existingFile != null) {
                File oldFile = new File(userDir, existingFile.getFileName() + ".enc");
                if (oldFile.exists()) {
                    oldFile.delete();
                }
                fileMetadataRepository.delete(existingFile); // Remove old metadata
            }

            // Encrypt and save the new file
            byte[] encryptedData = encryptionService.encryptFile(file.getBytes());
            File encryptedFile = new File(userDir, encryptedFileName);
            try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
                fos.write(encryptedData);
            }

            // Save new metadata
            FileMetadata newMetadata = new FileMetadata(userId, file.getOriginalFilename(), fileType);
            fileMetadataRepository.save(newMetadata);

            return ResponseEntity.ok("File uploaded successfully: " + encryptedFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<FileMetadata>> listFiles(@PathVariable Long userId) {
        List<FileMetadata> files = fileMetadataRepository.findByUserId(userId);

        if (files.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{userId}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable("userId") Long userId, @PathVariable("filename") String fileCode) {
        try {
            Resource encryptedResource = fileDownloadUtil.getFileAsResource(userId, fileCode + ".enc");

            if (encryptedResource == null || !encryptedResource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
            byte[] encryptedData = Files.readAllBytes(encryptedResource.getFile().toPath());
            byte[] decryptedData = encryptionService.decryptFile(encryptedData);

            ByteArrayResource decryptedResource = new ByteArrayResource(decryptedData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileCode + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(decryptedResource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error reading file");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error decrypting file");
        }
    }

    // VIEW FILE API
    @GetMapping("/view/{userId}/{filename}")
    public ResponseEntity<?> viewFile(@PathVariable("userId") Long userId, @PathVariable("filename") String fileName) {
        try {
            Resource encryptedResource = fileDownloadUtil.getFileAsResource(userId, fileName + ".enc");

            if (encryptedResource == null || !encryptedResource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            byte[] encryptedData = Files.readAllBytes(encryptedResource.getFile().toPath());
            byte[] decryptedData = encryptionService.decryptFile(encryptedData);

            ByteArrayResource decryptedResource = new ByteArrayResource(decryptedData);
            String contentType = determineContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(decryptedResource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error reading file");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error decrypting file");
        }
    }

    // NEW DELETE FILE API
    @DeleteMapping("/delete/{userId}/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable("userId") Long userId, @PathVariable("filename") String fileName) {
        try {
            FileMetadata fileMetadata = fileMetadataRepository.findByUserIdAndFileName(userId, fileName);
            if (fileMetadata == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found in metadata");
            }

            File encryptedFile = new File(UPLOAD_DIR + userId + "/" + fileName + ".enc");
            if (encryptedFile.exists()) {
                if (!encryptedFile.delete()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found in storage");
            }

            // Remove metadata from the database
            fileMetadataRepository.delete(fileMetadata);

            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + e.getMessage());
        }
    }

    public String determineContentType(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF_VALUE;
        } else if (lowerFileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerFileName.endsWith(".txt")) {
            return MediaType.TEXT_PLAIN_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default binary stream
        }
    }
}
