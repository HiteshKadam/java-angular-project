package com.idpr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    @JsonProperty("file")
    private MultipartFile file;
    @JsonProperty("userId")
    private Long userId;
}
