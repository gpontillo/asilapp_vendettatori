package com.vendettatori.asilapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class InputUtils {
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
