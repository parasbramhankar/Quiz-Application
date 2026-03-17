package com.example.user_service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileStorageService {


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadProfilePicture(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPG and PNG allowed");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Max file size is 2MB");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + originalName;

        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.createDirectories(uploadPath);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        return "/uploads/profile-pics/" + fileName;
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null) return;

        try {
            Path path = Paths.get(fileUrl.replace("/uploads/profile-pics/", uploadDir + "/"));
            Files.deleteIfExists(path);
        } catch (IOException ignored) {}
    }
}
