package com.example.boiler_commslogin.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class EditLoginResult {
    @Nullable
    private EditLoggedInUserView success;
    @Nullable
    private Integer error;

    EditLoginResult(@Nullable Integer error) {
        this.error = error;
    }

    EditLoginResult(@Nullable EditLoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    EditLoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}