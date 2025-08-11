package com.wanted.needswork.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {
    public String storeLogo(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // создаём папку, если нет
        Path uploadPath = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "static", "logo"
        ).toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);

        // уникальное имя
        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path target = uploadPath.resolve(filename);
        System.out.println(target);

        // сохраняем
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
