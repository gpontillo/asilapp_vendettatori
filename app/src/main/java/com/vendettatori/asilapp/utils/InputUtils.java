package com.vendettatori.asilapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class InputUtils {
    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPhone(String target) {
        return (!TextUtils.isEmpty(target) && target.length() <= 10);
    }

    public static String formatStringFromTimestamp(Timestamp target) {
        Date date = target.toDate();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public static Timestamp formatTimestampFromString(String target) {
        LocalDate dateLocale = LocalDate.parse(target, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        Date date = Date.from(dateLocale.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new Timestamp(date);
    }
}
