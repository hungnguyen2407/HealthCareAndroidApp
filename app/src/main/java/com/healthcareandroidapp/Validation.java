package com.healthcareandroidapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hungnguyen on 23/06/2017.
 */

public class Validation {


    //Check valid methods

    public static boolean isUserNameValid(String userName) {

        return userName.length() >= 6;
    }

    public static boolean isPasswordValid(String password) {

        return password.length() >= 4;

    }

    public static boolean isEmailValid(String email) {
        if (email != null) {
            Pattern p = Pattern.compile("^[A-Za-z].*?@*\\.*");
            Matcher m = p.matcher(email);
            return m.find();
        }
        return false;
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() > 10;
    }

    public static boolean isExperienceValid(String experience)
    {
        try {
            Integer.parseInt(experience);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}
