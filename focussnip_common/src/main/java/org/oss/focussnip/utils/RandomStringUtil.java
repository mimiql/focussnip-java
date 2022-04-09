package org.oss.focussnip.utils;

import java.util.Random;

public class RandomStringUtil {
    public static String getRandomString(int length){
        String str="0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i = 0 ; i < length ; i++){
            int number=random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
