package com.jooyunghan.parser;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class RegexTest {
    @Test
    public void lookingAt() throws Exception {
        Pattern abc = Pattern.compile("abc");

        assertFalse("lookingAt should match from the beginning of input", abc.matcher("xabcd").lookingAt());
        assertTrue("lookingAt should match from the beginning of input", abc.matcher("abcd").lookingAt());

        assertFalse("matches should match from the beginning of input to the end", abc.matcher("xabcd").matches());
        assertFalse("matches should match from the beginning of input to the end", abc.matcher("abcd").matches());
        assertTrue("matches should match from the beginning of input to the end", abc.matcher("abc").matches());

        assertTrue("find scans the match", abc.matcher("xabcd").find());
        assertTrue("find scans the match", abc.matcher("abcd").find());
    }
}
