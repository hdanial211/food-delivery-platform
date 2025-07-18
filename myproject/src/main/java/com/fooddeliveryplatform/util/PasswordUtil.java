package com.fooddeliveryplatform.util;

import org.mindrot.jbcrypt.BCrypt;

import com.fooddeliveryplatform.config.AppConfig;

public class PasswordUtil {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password + AppConfig.getPasswordPepper(), 
                           BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password + AppConfig.getPasswordPepper(), 
                            hashedPassword);
    }
}