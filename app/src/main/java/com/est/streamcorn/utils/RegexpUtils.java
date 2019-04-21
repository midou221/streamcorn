package com.est.streamcorn.utils;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {

    @Nullable
    public static String getFirstMatch(Pattern p, String string) {
        Matcher m = p.matcher(string);
        if (m.find())
            return m.group(1);
        else
            return null;
    }

}
