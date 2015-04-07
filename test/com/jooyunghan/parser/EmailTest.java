package com.jooyunghan.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class EmailTest {
    @Test
    public void decodeAll() throws Exception {
        assertEquals("ab", Email.decodeAll("=?ISO-8859-1?Q?a?= \n" +
                "       =?ISO-8859-1?Q?b?="));
    }

}