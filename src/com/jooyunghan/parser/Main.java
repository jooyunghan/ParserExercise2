package com.jooyunghan.parser;

import fp.Lists;
import fp.Pair;
import fp.Strings;
import fp.functions.Function;
import fp.parser.Parser;

import java.util.Map;

import static fp.Pair.pair;
import static fp.parser.Parser.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

        assertThat(getEmailParser1().parse(message), is(getEmailParser2().parse(message)));
    }

    private static Parser<Email> getEmailParser2() {
        // email = header "\n\n" body
        // header = field*
        // field = (line _line*) and then (key ':' value)
        // _line = space* line
        Parser<String> line = regex("[^\\n]+\\n").map(s -> s.trim());
        Parser<String> _line = regex("[ \\t]+").flatMap(ignore -> line);
        Parser<String> field = seq(line, _line.many())
                .map((pair) -> Lists.cons(pair._1, pair._2))
                .map(list -> Lists.toString(list, " "));
        Parser<Map<String, String>> header = field.many()
                .map(lists -> Lists.map(Strings.split(": "), lists))
                .map(pairs -> Lists.toMap(pairs));
        Parser<Email> email = seq(header, string("\n\n"), Parser.rest)
                .map(triple -> new Email(triple._1, triple._3));
        return email;
    }

    private static Parser<Email> getEmailParser1() {
        Parser<Character> newline = char_('\n');
        Parser<Character> noNewline = item.filter(c -> c != '\n');
        Parser<Character> spaceOrTab = char_(' ').or(char_('\t'));

        Parser<Character> contentChar =
                noNewline.or(newline.followedBy(spaceOrTab));

        Parser<Pair<String, String>> fieldParser =
                Parser.seq(
                        regex("[a-zA-Z-]+"),   // Key
                        string(": "),
                        contentChar.many().map(Lists::toString).map(replaceAll("\\n[ \\t]*", " ")),
                        newline
                ).map(q -> pair(q._1, q._3));

        Parser<Map<String, String>> headerParser =
                fieldParser.many().map(lists -> Lists.toMap(lists));

        return Parser.seq(
                headerParser,
                string("\n\n"),
                regex(".*")
        ).map(triple -> new Email(triple._1, triple._3));
    }

    private static Function<String, String> replaceAll(String regex, String replacement) {
        return s -> s.replaceAll(regex, replacement);
    }


}
