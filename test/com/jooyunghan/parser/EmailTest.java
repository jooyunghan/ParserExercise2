package com.jooyunghan.parser;

import com.sun.prism.impl.ps.CachingEllipseRep;
import fp.Option;
import fp.Pair;
import fp.parser.Parser;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fp.Lists.map;
import static fp.Option.none;
import static fp.Option.some;
import static fp.parser.Parser.*;
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

    @Test
    public void regexForEmailAddress() throws Exception {
        String addressLinePattern = "([^<]*)<([^>]+)>";
        String toLine = "=?UTF-8?B?J+q5gOyaqeyasSc=?= <yonguk.kim@lge.com>";
        Pattern p = Pattern.compile(addressLinePattern);
        Matcher matcher = p.matcher(toLine);
        assertTrue(matcher.matches());
        assertEquals("=?UTF-8?B?J+q5gOyaqeyasSc=?= ", matcher.group(1));
        assertEquals("yonguk.kim@lge.com", matcher.group(2));
    }

    static <T> Parser<T> or(Parser<T>... ps) {
        return (s) -> {
            for (Parser<T> p : ps) {
                Option<Pair<T, String>> parse = p.parse(s);
                if (parse.isSome()) return parse;
            }
            return none();
        };
    }

    @Test
    public void parseDate() throws Exception {
        // Sat, "4 Apr 2015 09:51:51 +0900"   == integer space monthname space integer space timeformat space timezone
        // line = ... format1
        // Friday, April 03, 2015 9:04 PM
        // 2015/04/06 15:41:45

        Parser<List<Integer>> time =
                seq(
                        regex("\\d(\\d)?:\\d\\d(:\\d\\d)?").map(s -> {
                            List<Integer> values = map(Integer::valueOf, Arrays.asList(s.split(":")));
                            if (values.size() == 2)
                                values.add(0);
                            return values;
                        }),
                        regex("([AaPp][Mm]?)?").map(s -> s.matches("[Pp][Mm]?")).token()
                ).map(pair ->
                        {
                            if (pair._2) pair._1.set(0, pair._1.get(0) + 12);
                            return pair._1;
                        }
                ).token();

        assertEquals(Arrays.asList(9, 51, 51), time.parse("09:51:51").get()._1);

        Parser<Integer> month = or(
                regex("Jan(uary)?").map(s -> 0),
                regex("Feb(urary)?").map(s -> 1),
                regex("Mar(ch)?").map(s -> 2),
                regex("Apr(il)?").map(s -> 3),
                regex("May").map(s -> 4),
                regex("Jun(e)?").map(s -> 5),
                regex("Jul(y)?").map(s -> 6),
                regex("Aug(ust)?").map(s -> 7),
                regex("Sep(tember)?").map(s -> 8),
                regex("Oct(ober)?").map(s -> 9),
                regex("Nov(ember)?").map(s -> 10),
                regex("Dec(ember)?").map(s -> 11)
        ).token();
        assertEquals(3, (int) month.parse("Apr").get()._1);
        assertEquals(3, (int) month.parse("April").get()._1);
        assertEquals(Arrays.asList(0, 1, 2), month.sepBy(char_(',')).parse("Jan,Feburary,Mar").get()._1);

        Parser<Integer> year = regex("\\d{4}").map(Integer::parseInt).token();
        assertEquals(2015, (int) year.parse("2015").get()._1);

        Parser<Integer> day = regex("\\d{1,2}").map(Integer::parseInt).token();
        assertEquals(3, (int) day.parse("03").get()._1);

        Parser<Calendar> format1 = seq(day, month, year, time).map(q ->
                new GregorianCalendar(q._3, q._2, q._1, q._4.get(0), q._4.get(1), q._4.get(2)));

        assertEquals(new GregorianCalendar(2015, 3, 4, 9, 51, 51), format1.parse("4 Apr 2015 09:51:51").get()._1);

        Option<Calendar> found = find(format1, "Sat, \"4 Apr 2015 09:51:51 +0900\" ");
        assertEquals(some(new GregorianCalendar(2015, 3, 4, 9, 51, 51)), found);

        Parser<Calendar> format2 = seq(month, day.suffix(char_(',')), year, time)
                .map(q -> new GregorianCalendar(q._3, q._1, q._2, q._4.get(0), q._4.get(1), q._4.get(2)));

        assertEquals(some(new GregorianCalendar(2015, 3, 3, 21, 4, 0)), find(format2, "Friday, April 03, 2015 9:04 PM "));

        Parser<Calendar> format3 = regex("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}").map(s -> {
                    try {
                        return fromDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(s));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
        Parser<Calendar> date = or(format1, format2, format3);
        assertEquals(some(new GregorianCalendar(2015, 3, 4, 9, 51, 51)), find(date, "Sat, \"4 Apr 2015 09:51:51 +0900\" "));
        assertEquals(some(new GregorianCalendar(2015, 3, 3, 21, 4, 0)), find(date, "Friday, April 03, 2015 9:04 PM "));
        assertEquals(some(new GregorianCalendar(2015, 3, 6, 15, 41, 45)), find(date, " 2015/04/06 15:41:45 "));
    }

    static Calendar fromDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }

    private <T> Option<T> find(Parser<T> p, String s) {
        while (!s.isEmpty()) {
            Option<Pair<T, String>> result = p.parse(s);
            if (result.isSome()) return result.map(pair -> pair._1);
            s = s.substring(1);
        }
        return none();
    }

}