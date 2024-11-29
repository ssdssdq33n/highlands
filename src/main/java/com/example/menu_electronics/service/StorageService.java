package com.example.menu_electronics.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String getStoredFilename(MultipartFile file, String id);

    void store(MultipartFile file, String storedFilename);

    org.springframework.core.io.Resource loadAsResource(String filename);

    Path load(String filename);

    void delete(String storedFilename) throws IOException;

    void init();
}
