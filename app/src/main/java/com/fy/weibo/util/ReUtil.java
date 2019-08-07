package com.fy.weibo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fan on 2018/8/3.
 * Fighting!!!
 */
public final class ReUtil {

    //   匹配微博来源
    public static String getSource(String source) {

        return base(">(.*)<", source);
    }


    private static String base(String regEx, String text) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static String getUrl(String text) {
        return base(".*((http|https).*)", text);
    }

    public static String getText(String text) {
        return base("(.*)http", text);
    }
}

/*
正则工具类
 */