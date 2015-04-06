package com.jooyunghan.parser;

import fp.Lists;
import fp.Strings;

import java.util.Map;

/**
 * User: jooyunghan
 * Date: 4/7/15 1:57 AM
 */
class Email {
    final Map<String, String> header;
    final String body;

    public Email(Map<String, String> header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Email{\n" +
                "header=" + Lists.toString(header.entrySet(), "\n") + "\n" +
                ", body='" + Strings.take(10, body) + "...\'\n" +
                '}';
    }
}
