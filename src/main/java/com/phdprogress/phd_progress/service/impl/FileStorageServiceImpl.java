package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.exception.FileStorageException;
import com.phdprogress.phd_progress.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "docx");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final Path rootLocation;

    public FileStorageServiceImpl(@Value("${app.storage.root}") String rootPath) {
        this.rootLocation = Paths.get(rootPath).toAbsolutePath().normalize();
    }

    @Override
    public StoredFile storeFile(Long submissionId, MultipartFile file) {
        validateFile(file);
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = extractExtension(originalFilename);
        String storedFilename = UUID.randomUUID() + "." + extension;

        Path submissionDirectory = rootLocation.resolve(Paths.get("submissions", String.valueOf(submissionId))).normalize();
        Path targetFile = submissionDirectory.resolve(storedFilename).normalize();

        if (!targetFile.startsWith(submissionDirectory)) {
            throw new FileStorageException("Invalid file path");
        }

        try {
            Files.createDirectories(submissionDirectory);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return new StoredFile(originalFilename, rootLocation.relativize(targetFile).toString(), file.getContentType(), file.getSize());
        } catch (IOException exception) {
            throw new FileStorageException("Failed to store submission file", exception);
        }
    }

    @Override
    public Resource getFile(String filePath) {
        try {
            Path resolvedPath = resolveStoredPath(filePath);
            Resource resource = new UrlResource(resolvedPath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileStorageException("Stored file is not readable");
            }
            return resource;
        } catch (MalformedURLException exception) {
            throw new FileStorageException("Failed to read stored file", exception);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            Files.deleteIfExists(resolveStoredPath(filePath));
        } catch (IOException exception) {
            throw new FileStorageException("Failed to delete stored file", exception);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Uploaded file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("Uploaded file exceeds maximum size of 10MB");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = extractExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileStorageException("Only PDF and DOCX files are allowed");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new FileStorageException("Only PDF and DOCX files are allowed");
        }
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            throw new FileStorageException("Uploaded file must have a valid extension");
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    private Path resolveStoredPath(String filePath) {
        Path resolvedPath = rootLocation.resolve(filePath).normalize();
        if (!resolvedPath.startsWith(rootLocation)) {
            throw new FileStorageException("Invalid stored file path");
        }
        return resolvedPath;
    }
}
