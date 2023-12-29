package com.vendettatori.asilapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class InputUtils {
    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPhone(String target) {
        return (!TextUtils.isEmpty(target) && target.length() <= 10);
    }
}
