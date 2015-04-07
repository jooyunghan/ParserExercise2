package com.jooyunghan.parser;

import fp.Lists;
import fp.Pair;
import fp.Strings;
import fp.functions.Function;
import fp.parser.Parser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static fp.Pair.pair;
import static fp.parser.Parser.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Main {

    public static void main(String[] args) throws IOException {
        //testParsers();

        String message = Lists.toString(FileUtils.readLines(new File("testdata/seminar.eml")), "\n");
        getEmailParser1()
                .tap(m -> System.out.println(m.getSubject()))
                .parse(message);
        //assertThat(getEmailParser1().parse(message), is(getEmailParser2().parse(message)));
    }

    private static Parser<Email> getEmailParser2() {
        // email = header "\n\n" body
        // header = field*
        // field = (line _line*) and then (key ':' value)
        // _line = space* line
        Parser<String> line = regex("[^\\n]+\\n").map(String::trim);
        Parser<String> _line = regex("[ \\t]+").flatMap(ignore -> line);
        Parser<String> field = seq(line, _line.many())
                .map((pair) -> Lists.cons(pair._1, pair._2))
                .map(list -> Lists.toString(list, " "));
        Parser<Map<String, String>> header = field.many()
                .map(lists -> Lists.map(Strings.split(": "), lists))
                .map(Lists::toMap);
        return seq(
                header,
                string("\n"),
                Parser.rest
        ).map(triple -> new Email(triple._1, triple._3));
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
                fieldParser.many().map(Lists::toMap);

        return Parser.seq(
                headerParser,
                string("\n"),
                rest
        ).map(triple -> new Email(triple._1, triple._3));
    }

    private static Function<String, String> replaceAll(String regex, String replacement) {
        return s -> s.replaceAll(regex, replacement);
    }


}
