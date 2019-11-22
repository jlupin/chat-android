package com.example.chat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Piotr Heilman
 */
public class DateUtil {
    public static String formatDate(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                Locale.getDefault()
        );
        return simpleDateFormat.format(date);
    }
}
