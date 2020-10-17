package com.example.boiler_commslogin.sign_up;

public class PasswordHasher {
    private String password = "";
    public PasswordHasher(String s) {
        password = s;
    }
    public String hashPass() {
        //Valid password must be >= 9 and <= 22 characters and contain characters and numbers
        if (password.length() < 9 || password.length() > 22) {
            return "";
        }
        String numRegex   = ".*[0-9].*";
        String alphaRegex = ".*[A-Z].*";
        String alphaRegex2 = ".*[a-z].*";
        if (!password.matches(numRegex) || !(password.matches(alphaRegex) ||
                password.matches(alphaRegex2))) {
            return "";
        }
        int hashedpass = password.hashCode();
        return "" + hashedpass;
    }
}