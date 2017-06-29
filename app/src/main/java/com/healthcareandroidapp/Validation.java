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
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() > 10;
    }

    public static boolean isExperienceValid(String experience) {
        try {
            Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isBirthDate(String date, String month, String year) {

        try {
            int dateNum = Integer.parseInt(date);
            int monthNum = Integer.parseInt(month);
            int yearNum = Integer.parseInt(year);

            if (dateNum < 0 || dateNum > 31)
                return false;
            if (monthNum < 0 || monthNum > 12)
                return false;
            if (yearNum < 0 || yearNum > 2100)
                return false;

            switch (monthNum)
            {
                case 1:
                    if(dateNum>31)
                        return false;
                case 2:
                    if (yearNum%4==0&&yearNum%100!=0)
                        if(dateNum>29)
                            return false;
                    else
                        if(dateNum>28)
                            return false;
                case 3:
                    if(dateNum>31)
                        return false;
                case 4:
                    if (dateNum>30)
                        return false;
                case 5:
                    if(dateNum>31)
                        return false;
                case 6:
                    if(dateNum>30)
                        return false;
                case 7:
                    if(dateNum>31)
                        return false;
                case 8:
                    if (dateNum>31)
                        return false;
                case 9:
                    if(dateNum>30)
                        return false;
                case 10:
                    if(dateNum>31)
                        return false;
                case 11:
                    if(dateNum>30)
                        return false;
                case 12:
                    if(dateNum>31)
                        return false;
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

}
