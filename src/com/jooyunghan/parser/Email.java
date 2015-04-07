package com.jooyunghan.parser;

import fp.Lists;
import fp.Strings;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static fp.Lists.map;
import static fp.parser.Parser.*;

/**
 * User: jooyunghan
 * Date: 4/7/15 1:57 AM
 */
public class Email {
    private final Map<String, String> header;
    private final String body;

    public Email(Map<String, String> header, String body) {
        this.header = header;
        this.body = body;
    }

    public List<Contact> getTo() {
        String toLine = header.get("To");
        return map(address -> parseAddress(address), Arrays.asList(toLine.split(",")));
    }

    public List<Contact> getCc() {
        String toLine = header.get("CC");
        return map(address -> parseAddress(address), Arrays.asList(toLine.split(",")));
    }

    public String getSubject() {
        return decodeAll(header.get("Subject"));
    }

    private Contact parseAddress(String address) {
        return seq(regex("[^<]*").map(s -> decodeAll(s))
                , char_('<')
                , regex("[^>]*")
                , char_('>'))
                .map(q -> new Contact(q._3, q._1)).parse(address).get()._1;
    }

    public static String decodeAll(String s) {
        StringBuilder sb = new StringBuilder();
        int start = 0;
        int pos = s.indexOf("=?");
        while (pos >= 0) {
            sb.append(s.substring(start, pos).trim());
            int end = s.indexOf("?=", pos + 2);
            sb.append(decode(s.substring(pos + 2, end)));
            start = end + 2;
            pos = s.indexOf("=?", start);
        }
        sb.append(s.substring(start).trim());
        return sb.toString();
    }

    private static String decode(String s) {
        String[] splits = s.split("\\?");
        assert splits.length == 3;
        assert splits[1].equals("Q") || splits[1].equals("B");

        byte[] result;
        if (splits[1].equals("B"))
            result = Base64.getDecoder().decode(splits[2]);
        else
            result = QuotedPrintable.getDecoder().decode(splits[2]);

        return new String(result, Charset.forName(splits[0]));
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
