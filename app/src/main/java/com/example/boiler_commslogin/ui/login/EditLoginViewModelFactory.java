package com.example.boiler_commslogin.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.boiler_commslogin.ui.login.EditUser_LoginViewModel;
import com.example.boiler_commslogin.data.EditLoginDataSource;
import com.example.boiler_commslogin.data.EditLoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class EditLoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditUser_LoginViewModel.class)) {
            return (T) new EditUser_LoginViewModel(EditLoginRepository.getInstance(new EditLoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}