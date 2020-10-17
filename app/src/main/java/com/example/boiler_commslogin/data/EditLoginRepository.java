package com.example.boiler_commslogin.data;

import com.example.boiler_commslogin.data.model.EditLoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class EditLoginRepository {

    private static volatile EditLoginRepository instance;

    private EditLoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private EditLoggedInUser user = null;

    // private constructor : singleton access
    private EditLoginRepository(EditLoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static EditLoginRepository getInstance(EditLoginDataSource dataSource) {
        if (instance == null) {
            instance = new EditLoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(EditLoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<EditLoggedInUser> login(String username, String password, String confirmPassword) {
        // handle login
        Result<EditLoggedInUser> result = dataSource.login(username, password, confirmPassword);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<EditLoggedInUser>) result).getData());
        }
        return result;
    }
}