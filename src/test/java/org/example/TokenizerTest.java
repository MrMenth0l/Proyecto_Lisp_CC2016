package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {

    private final LispTokenizer tokenizer = new LispTokenizer();

    @Test
    public void testSimpleExpression() {
        List<String> tokens = tokenizer.tokenize("(+ 1 2)");
        assertEquals(List.of("(", "+", "1", "2", ")"), tokens);
    }

    @Test
    public void testNestedExpression() {
        List<String> tokens = tokenizer.tokenize("(+ 1 (* 2 3))");
        assertEquals(List.of("(", "+", "1", "(", "*", "2", "3", ")", ")"), tokens);
    }

    @Test
    public void testExtraSpaces() {
        List<String> tokens = tokenizer.tokenize("  ( +   4     5 )  ");
        assertEquals(List.of("(", "+", "4", "5", ")"), tokens);
    }

    @Test
    public void testEmptyInput() {
        List<String> tokens = tokenizer.tokenize("");
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void testOnlyParentheses() {
        List<String> tokens = tokenizer.tokenize("()");
        assertEquals(List.of("(", ")"), tokens);
    }
}
