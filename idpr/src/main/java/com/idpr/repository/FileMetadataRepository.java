package com.idpr.repository;

import com.idpr.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByUserId(Long userId);
    FileMetadata findByUserIdAndFileType(Long userId, String fileType);
    FileMetadata findByUserIdAndFileName(Long userId, String fileName);
}
