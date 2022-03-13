package org.oss.focussnip.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String EncodePassword(String password){
        return  bCryptPasswordEncoder.encode(password);
    }

    public static boolean matchesPassword(String password, String encodePassword){
        return bCryptPasswordEncoder.matches(password,encodePassword);
    }
}
