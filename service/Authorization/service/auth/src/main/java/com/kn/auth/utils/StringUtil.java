package com.kn.auth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String extractSubstring(String root, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(root);

        String constraintName = "";
        if (matcher.find()) {
            constraintName = matcher.group(1);
            try {
                constraintName = constraintName.split("\\.")[1];
                return constraintName;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
        return "";
    }
}
