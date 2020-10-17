package com.example.boiler_commslogin.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class EditLoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    EditLoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}