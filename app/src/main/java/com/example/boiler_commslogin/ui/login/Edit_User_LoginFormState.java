package com.example.boiler_commslogin.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class Edit_User_LoginFromState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private Integer confirmPassWordError;
    private boolean isDataValid;

    Edit_User_LoginFromState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmPasswordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPassWordError = confirmPasswordError;
        this.isDataValid = false;
    }

    Edit_User_LoginFromState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }
    @Nullable
    Integer getConfirmPasswordError() {
        return confirmPassWordError;
    }
    boolean isDataValid() {
        return isDataValid;
    }
}