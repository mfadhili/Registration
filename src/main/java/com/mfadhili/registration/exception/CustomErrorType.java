package com.mfadhili.registration.exception;

import com.mfadhili.registration.dto.Users;

public class CustomErrorType extends Users {
    private String errorMessage;

    public CustomErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
