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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        if (!header.equals(email.header)) return false;
        return body.equals(email.body);

    }

    @Override
    public int hashCode() {
        int result = header.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}
