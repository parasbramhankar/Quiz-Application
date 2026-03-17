package com.example.user_service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadProfilePicture(MultipartFile file);

    void deleteFile(String fileUrl);
}