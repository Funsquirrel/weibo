package com.fy.weibo;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUrl() {

        String regEx = "\\[(.*)]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher("[12444dfd]");
//        Matcher matcher = pattern.matcher("[{\"id\":5164746914,\"followers_count\":41,\"friends_count\":16,\"statuses_count\":0,\"private_friends_count\":0,\"pagefriends_count\":0}]");
        System.out.println(matcher.find() ? matcher.group(1) : "没有匹配");
    }
}