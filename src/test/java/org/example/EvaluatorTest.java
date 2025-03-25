package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    @Test
    public void testSimpleNumber() {
        LispEvaluator evaluator = new LispEvaluator();
        assertEquals(42.0, evaluator.evaluate(List.of("42")));
    }

    @Test
    public void testAddition() {
        LispEvaluator evaluator = new LispEvaluator();
        assertEquals(10.0, evaluator.evaluate(List.of("+", "4", "6")));
    }

    @Test
    public void testSetqAndUse() {
        LispEvaluator evaluator = new LispEvaluator();
        evaluator.evaluate(List.of("SETQ", "X", "10"));
        assertEquals(15.0, evaluator.evaluate(List.of("+", "X", "5")));
    }

    @Test
    public void testEqualComparison() {
        LispEvaluator evaluator = new LispEvaluator();
        assertEquals(1.0, evaluator.evaluate(List.of("EQUAL", "5", "5")));
    }

    @Test
    public void testLessThanComparison() {
        LispEvaluator evaluator = new LispEvaluator();
        assertEquals(1.0, evaluator.evaluate(List.of("<", "3", "9")));
    }

    @Test
    public void testCondTrueFirst() {
        LispEvaluator evaluator = new LispEvaluator();
        assertEquals(100.0, evaluator.evaluate(List.of("COND", "(", ">", "10", "5", "100", ")", "(", "<", "3", "4", "200", ")")));
    }

    @Test
    public void testDefunAndCall() {
        LispEvaluator evaluator = new LispEvaluator();
        evaluator.evaluate(List.of("DEFUN", "suma3", "(", "a", ")", "+", "a", "3", ")"));
        assertEquals(8.0, evaluator.evaluate(List.of("suma3", "5")));
    }

    @Test
    public void testInvalidOperationThrows() {
        LispEvaluator evaluator = new LispEvaluator();
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> evaluator.evaluate(List.of("FOO", "1", "2")));
        assertTrue(ex.getMessage().contains("no es una operación válida"));
    }

    @Test
    public void testUnbalancedParenthesesThrows() {
        LispEvaluator evaluator = new LispEvaluator();
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> evaluator.evaluate(List.of("(", "+", "1", "2")));
        assertTrue(ex.getMessage().contains("paréntesis"));
    }

    @Test
    public void testDefunWrongArgsThrows() {
        LispEvaluator evaluator = new LispEvaluator();
        evaluator.evaluate(List.of("DEFUN", "doble", "(", "x", ")", "*", "x", "2", ")"));
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> evaluator.evaluate(List.of("doble")));
        assertTrue(ex.getMessage().contains("número de argumentos incorrecto"));
    }
}
