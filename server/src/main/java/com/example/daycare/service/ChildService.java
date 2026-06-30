package com.example.daycare.service;

import com.example.daycare.dto.ChildDto;
import com.example.daycare.repository.ChildRepository;
import com.example.daycare.model.Child;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final MapStructMapper mapper;
    private final Path imageStorageDir;

    public ChildService(ChildRepository childRepository,
                        MapStructMapper mapper,
                        @Value("${app.image-storage-dir:images}") String imageStorageDir) {
        this.childRepository = childRepository;
        this.mapper = mapper;
        this.imageStorageDir = Paths.get(imageStorageDir).toAbsolutePath().normalize();
    }

    @Transactional(readOnly = true)
    public List<ChildDto> getAll() {
        return mapper.toListChildDto(childRepository.findAll());
    }

    @Transactional
    public ChildDto add(ChildDto childDto) {
        final Child child = mapper.toChild(childDto);
        return mapper.toChildDto(childRepository.save(child));
    }

    @Transactional
    public ChildDto addWithImage(MultipartFile file, ChildDto childDto) {
        final String storedFileName = storeImage(file);
        final Child child = mapper.toChild(childDto);
        child.setImageChild(storedFileName);
        return mapper.toChildDto(childRepository.save(child));
    }

    /**
     * Persists the uploaded image under a generated, collision-free name. The
     * client-supplied filename is never used for the stored path, and the
     * resolved target is verified to stay within the configured storage
     * directory, which prevents path-traversal attacks.
     */
    private String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        final String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        final String extension = extractExtension(originalName);
        final String storedFileName = UUID.randomUUID() + extension;

        final Path targetPath = imageStorageDir.resolve(storedFileName).normalize();
        if (!targetPath.startsWith(imageStorageDir)) {
            throw new IllegalArgumentException("Resolved file path is outside the storage directory");
        }

        try {
            Files.createDirectories(imageStorageDir);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to store uploaded image", ex);
        }

        return storedFileName;
    }

    private String extractExtension(String fileName) {
        final int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex).toLowerCase();
    }
}
