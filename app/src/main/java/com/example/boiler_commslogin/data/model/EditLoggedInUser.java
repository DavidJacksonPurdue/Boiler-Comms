package com.example.boiler_commslogin.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class EditLoggedInUser {

    private String userId;
    private String displayName;

    public EditLoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}