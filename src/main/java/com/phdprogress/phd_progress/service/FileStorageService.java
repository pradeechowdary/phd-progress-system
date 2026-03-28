package com.phdprogress.phd_progress.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    StoredFile storeFile(Long submissionId, MultipartFile file);

    Resource getFile(String filePath);

    void deleteFile(String filePath);

    class StoredFile {
        private final String fileName;
        private final String filePath;
        private final String fileType;
        private final long fileSize;

        public StoredFile(String fileName, String filePath, String fileType, long fileSize) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.fileType = fileType;
            this.fileSize = fileSize;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getFileType() {
            return fileType;
        }

        public long getFileSize() {
            return fileSize;
        }
    }
}
