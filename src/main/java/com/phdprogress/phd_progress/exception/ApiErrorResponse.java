package com.phdprogress.phd_progress.exception;

import java.time.Instant;
import java.util.List;

public class ApiErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final List<String> details;

    public ApiErrorResponse(Instant timestamp, int status, String error, String message, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
