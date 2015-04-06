package com.jooyunghan.parser;

import java.util.Map;

import static com.jooyunghan.parser.Pair.pair;
import static com.jooyunghan.parser.Parser.*;

class Email {
    final Map<String, String> header;
    final String body;

    public Email(Map<String, String> header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "header=" + Lists.toString(header.entrySet(), "\n") +
                ", body='" + body + '\'' +
                '}';
    }
}

public class Main {

    public static void main(String[] args) {
        //testParsers();

        String message = "From: Jooyung Han <jooyung.han@lge.com>\n" +
                "Content-Type: multipart/alternative;\n" +
                "\tboundary=\"Apple-Mail=_931009DB-0296-42B9-A92F-90811810E4D0\"\n" +
                "Message-Id: <F5169255-BA0F-4653-85B8-AF750EC2F4DD@lge.com>\n" +
                "Mime-Version: 1.0 (Mac OS X Mail 8.2 \\(2070.6\\))\n" +
                "X-Priority: 3 (Normal)\n" +
                "Subject: =?utf-8?Q?Fwd=3A_=5B=EC=9D=B8=EC=9B=90_=EC=84=A0=EC=A0=95_?=\n" +
                " =?utf-8?Q?=EC=9A=94=EC=B2=AD=5D_Fw_=3A_FW=3A_=5BRemind=3A_?=\n" +
                " =?utf-8?Q?=ED=9A=8C=EC=9D=98_=EA=B3=B5=EC=A7=80=5D_=2715?=\n" +
                " =?utf-8?Q?=EB=85=84_CIP_Kick-off_=EB=AF=B8=ED=8C=85_=284/2=2C_11?=\n" +
                " =?utf-8?Q?=3A30=7E=29?=\n" +
                "X-Smtp-Server: lgekrhqmh01.lge.com\n" +
                "Date: Mon, 6 Apr 2015 15:41:45 +0900\n" +
                "References: <OFBBAD8FFF.A3EE4AA1-ON49257E1F.0020E817-49257E1F.0020E817@lge.com>\n" +
                "To: =?utf-8?B?6rCV7Jqp7ISx?= <yongsung.kang@lge.com>\n" +
                "X-Universally-Unique-Identifier: E3B5B664-CE47-4E3E-84C5-E066706A9ECA\n" +
                "\n" +
                "\n" +
                "--Apple-Mail=_931009DB-0296-42B9-A92F-90811810E4D0\n" +
                "Content-Transfer-Encoding: quoted-printable\n" +
                "Content-Type: text/plain;\n" +
                "\tcharset=utf-8\n";

        Parser<Character> contentChar =
                item.filter(c -> c != '\n').or(regex("\\n[ \\t]+").map(ignore -> ' '));
        Parser<Pair<String, String>> fieldParser =
                Parser.seq(
                        regex("[a-zA-Z-]+"),
                        string(": "),
                        contentChar.many()
                ).map(triple -> pair(triple._1, Lists.toString(triple._3)));
        Parser<Map<String, String>> headerParser =
                fieldParser.sepBy(string("\n")).map(Lists::toMap);
        Parser<Email> emailParser =
                Parser.seq(
                        headerParser,
                        string("\n\n"),
                        regex(".*")
                ).map(triple -> new Email(triple._1, triple._3));
        System.out.println(emailParser.parse(message));
    }

    private static void testParsers() {
        System.out.println(fail.parse("abc"));
        System.out.println(item.parse("abc"));

        Parser<Character> p = item.filter(c -> Character.isUpperCase(c));

        Parser<String> twoChars =
                item.flatMap(c1 -> item.map(c2 -> c1 + "" + c2));
        System.out.println(twoChars.parse("abc"));

        System.out.println(double_.parse("-123abc"));

        System.out.println(regex("[A-Z]{1,3}").parse("Date abc"));

        System.out.println(Character.isWhitespace('\n'));
        //System.out.println(Parser.item.followedBy(Parser.string("abc")).parse("1abc"));
    }
}
