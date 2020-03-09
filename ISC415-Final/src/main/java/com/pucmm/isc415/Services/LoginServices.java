package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Utils.MyParser;

import java.util.HashMap;
import java.util.Map;

public class LoginServices {

    public static boolean login(String username, String password) {

        boolean result = false;

        User testUser = UserServices.getInstance().get(username);

        if (testUser != null) {

            if (MyParser.comparePassword(password, testUser.getPassword())) {

                result = true;

            }
        }

        return result;
    }
}
