package fp.parser;

import fp.Option;
import org.junit.Test;

import static fp.Pair.pair;
import static fp.parser.Parser.regex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: jooyunghan
 * Date: 4/7/15 2:15 AM
 */
public class ParserTest {
    @Test
    public void regexWorksWithDotAllMode() throws Exception {
        assertThat(regex(".*").parse("\nabc"), is(Option.some(pair("\nabc", ""))));
    }
}