package com.evan.scanenhancer.data.model;

import com.evan.scanenhancer.util.Status;

public class ServiceResult {
    private final String message;
    private final Result result;
    private final Status status;

    public ServiceResult(String message, Result result, Status status) {
        this.message = message;
        this.result = result;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Result getResult() {
        return result;
    }
}
