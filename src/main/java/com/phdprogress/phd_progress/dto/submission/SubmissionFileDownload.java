package com.phdprogress.phd_progress.dto.submission;

import org.springframework.core.io.Resource;

public class SubmissionFileDownload {

    private final Resource resource;
    private final String fileName;
    private final String fileType;

    public SubmissionFileDownload(Resource resource, String fileName, String fileType) {
        this.resource = resource;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Resource getResource() {
        return resource;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }
}
