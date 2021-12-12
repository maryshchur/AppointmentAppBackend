package org.example.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile multipartFile);

    void deleteFile(String fileName);
}
