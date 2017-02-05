package ch.rmy.curlparser;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class CommandParserTest {

    @Test
    public void testParser() {
        String target = "curl -X \"Hello 'World'\" \"escaped \\\" quotes\" --bla 'foo'";
        String[] expected = new String[]{
                "curl", "-X", "Hello 'World'", "escaped \\\" quotes", "--bla", "foo"
        };
        List<String> arguments = CommandParser.parseCommand(target);
        String[] actual = arguments.toArray(new String[arguments.size()]);
        assertArrayEquals(expected, actual);
    }

}
