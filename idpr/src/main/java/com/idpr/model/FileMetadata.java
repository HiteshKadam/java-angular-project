package com.idpr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "file_metadata")
public class FileMetadata {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String fileName;
    private String fileType;  // e.g., Aadhaar, DOB, PAN

    public FileMetadata() {}

    public FileMetadata(Long userId, String fileName, String fileType) {
        this.userId = userId;
        this.fileName = fileName;
        this.fileType = fileType;
    }

}
