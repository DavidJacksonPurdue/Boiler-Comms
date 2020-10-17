package com.example.boiler_commslogin.data;

import com.example.boiler_commslogin.data.model.EditLoggedInUser;
import com.example.boiler_commslogin.data.model.EditLoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class EditLoginDataSource {

    public Result<EditLoggedInUser> login(String username, String password, String confirmPassword) {

        try {
            // TODO: handle loggedInUser authentication
            EditLoggedInUser fakeUser =
                    new EditLoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}