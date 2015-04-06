package fp.parser;

import fp.Lists;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static fp.Pair.pair;
import static fp.parser.Parser.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * User: jooyunghan
 * Date: 4/7/15 2:15 AM
 */
public class ParserTest {
    @Test
    public void testPrimitives() throws Exception {
        assertThat(fail, failsToParse("abc"));
        assertThat(item, parses("abc", 'a'));
        assertThat(double_, parses("-123abc", -123.0));
    }

    @Test
    public void testMapFilterFlatMap() throws Exception {
        assertThat(item.filter(c -> Character.isUpperCase(c)), parses("Abc", 'A'));
        Parser<String> twoChars =
                item.flatMap(c1 -> item.map(c2 -> c1 + "" + c2));
        assertThat(twoChars, parses("abc", "ab"));
    }

    @Test
    public void testRegex() throws Exception {
        assertThat(regex("[A-Z]{1,3}"), parses("Date abc", "D"));
        assertThat(regex(".*"), parses("\nabc", "\nabc"));      // DOTALL mode
    }

    private <T> Matcher<? super Parser<T>> parses(String s, T t) {
        return new TypeSafeMatcher<Parser<T>>() {
            @Override
            protected boolean matchesSafely(Parser<T> tParser) {
                return tParser.parse(s).filter(pair -> pair._1.equals(t)).isSome();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("should parse \"" + s +
                        "\" successfully with result of + \"" + t + "\"");
            }
        };
    }

    private Matcher<? super Parser<Object>> failsToParse(String s) {
        return new TypeSafeMatcher<Parser<Object>>() {
            @Override
            protected boolean matchesSafely(Parser<Object> objectParser) {
                return !objectParser.parse(s).isSome();
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    @Test
    public void mapEquals() throws Exception {
        Map<String, String> map1 = Lists.toMap(Arrays.asList(pair("a", "A"), pair("b", "B")));
        Map<String, String> map2 = Lists.toMap(Arrays.asList(pair("a", "A"), pair("b", "B")));
        assertEquals(map1, map2);
    }
}