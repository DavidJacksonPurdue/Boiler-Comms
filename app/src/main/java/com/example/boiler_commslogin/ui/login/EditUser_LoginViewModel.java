package com.example.boiler_commslogin.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.boiler_commslogin.data.EditLoginRepository;
import com.example.boiler_commslogin.data.LoginRepository;
import com.example.boiler_commslogin.data.Result;
import com.example.boiler_commslogin.data.model.EditLoggedInUser;
import com.example.boiler_commslogin.R;

public class EditUser_LoginViewModel extends ViewModel {

    private MutableLiveData<Edit_User_LoginFromState> Edit_User_LoginFromState = new MutableLiveData<>();
    private MutableLiveData<EditLoginResult> loginResult = new MutableLiveData<>();
    private EditLoginRepository loginRepository;

    EditUser_LoginViewModel(EditLoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<Edit_User_LoginFromState> getLoginFromState() {
        return Edit_User_LoginFromState;
    }

    LiveData<EditLoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String confirmPassword) {
        // can be launched in a separate asynchronous job
        Result<EditLoggedInUser> result = loginRepository.login(username, password, confirmPassword);

        if (result instanceof Result.Success) {
            EditLoggedInUser data = ((Result.Success<EditLoggedInUser>) result).getData();
            loginResult.setValue(new EditLoginResult(new EditLoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new EditLoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password, String confirmPassword) {
        boolean isValid = true;
        if (!isUserNameValid(username)) {
            Edit_User_LoginFromState.setValue(new Edit_User_LoginFromState(R.string.invalid_username, null, null));
            isValid = false;
        }
        if (!isPasswordValid(password)) {
            Edit_User_LoginFromState.setValue(new Edit_User_LoginFromState(null, R.string.invalid_password, null));
            isValid = false;
        }
        if (!isConfirmPasswordValid(password, confirmPassword)) {
            Edit_User_LoginFromState.setValue(new Edit_User_LoginFromState(null, null, R.string.invalid_confirm_password));
            isValid = false;
        }
        if(isValid){
            Edit_User_LoginFromState.setValue(new Edit_User_LoginFromState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 8;
    }
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return confirmPassword != null && confirmPassword.trim().length() > 8 && password.equals(confirmPassword);
    }
}