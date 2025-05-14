package com.idpr.controller;

import com.idpr.repository.FileMetadataRepository;
import com.idpr.service.FileEncryptionService;
import com.idpr.util.FileDownloadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Spy
    @InjectMocks
    private FileController fileController;

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private FileEncryptionService encryptionService;

    @Mock
    private FileDownloadUtil fileDownloadUtil;

    @ParameterizedTest
    @CsvSource({
            "document.pdf, application/pdf",
            "image.png, image/png",
            "photo.jpg, image/jpeg",
            "photo.jpeg, image/jpeg",
            "notes.txt, text/plain",
            "archive.zip, application/octet-stream",
            "unknownfile, application/octet-stream"
    })
    void determineContentType_withVariousFileExtensions_returnsExpectedContentType(String fileName, String expectedContentType) {
        String contentType = fileController.determineContentType(fileName);
        assertEquals(expectedContentType, contentType);
    }

    @ParameterizedTest
    @EmptySource
    void determineContentType_withNullOrEmptyFileName_returnsDefaultContentType(String fileName) {
        String contentType = fileController.determineContentType(fileName);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_VALUE, contentType);
    }
}