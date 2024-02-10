package com.poshtarenko.codeforge.entity.test;

public enum TaskCompletionStatus {

    NO_CODE("No code"),
    COMPILATION_ERROR("Compilation error"),
    TASK_FAILED("Task failed"),
    TASK_COMPLETED("Task completed");

    private final String message;

    TaskCompletionStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
