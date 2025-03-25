package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    private ITokenizer tokenizer;
    private LispEvaluator evaluator;

    @BeforeEach
    public void setup() {
        tokenizer = new LispTokenizer();
        evaluator = new LispEvaluator();
    }

    @Test
    public void testArithmeticNested() {
        assertEquals(11.0, evaluator.evaluate(tokenizer.tokenize("(+ 5 (* 2 3))")));
    }

    @Test
    public void testArithmeticMultiple() {
        assertEquals(18.0, evaluator.evaluate(tokenizer.tokenize("(+ 3 4 (* 2 3 ) 5 )")));
    }

    @Test
    public void testSetqAssignment() {
        assertEquals(10.0, evaluator.evaluate(tokenizer.tokenize("(SETQ X 10)")));
    }

    @Test
    public void testVariableUsage() {
        evaluator.evaluate(tokenizer.tokenize("(SETQ X 10)"));
        assertEquals(15.0, evaluator.evaluate(tokenizer.tokenize("(+ X 5)")));
    }

    @Test
    public void testEqualComparison() {
        assertEquals(1.0, evaluator.evaluate(tokenizer.tokenize("(EQUAL 10 10)")));
    }

    @Test
    public void testLessThanComparison() {
        assertEquals(1.0, evaluator.evaluate(tokenizer.tokenize("(< 5 10)")));
    }

    @Test
    public void testGreaterThanComparison() {
        assertEquals(1.0, evaluator.evaluate(tokenizer.tokenize("(> 15 7)")));
    }

    @Test
    public void testCondBasic() {
        assertEquals(100.0, evaluator.evaluate(tokenizer.tokenize("(COND ((> 10 5) 100) ((< 2 3) 200) (T 300))")));
    }

    @Test
    public void testCondWithSetq() {
        evaluator.evaluate(tokenizer.tokenize("(SETQ Y 8)"));
        assertEquals(60.0, evaluator.evaluate(tokenizer.tokenize("(COND ((< Y 5) 50) ((> Y 6) 60) (T 70))")));
    }

    @Test
    public void testCondWithExpressions() {
        evaluator.evaluate(tokenizer.tokenize("(SETQ Y 8)"));
        assertEquals(6.0, evaluator.evaluate(tokenizer.tokenize("(COND ((< Y 5) (+ 2 3)) ((> Y 6) (* 2 3)) (T (- 10 3)))")));
    }

    @Test
    public void testCondWithOnlyT() {
        assertEquals(999.0, evaluator.evaluate(tokenizer.tokenize("(COND ((T) 999))")));
    }

    @Test
    public void testDefunCuadrado() {
        assertEquals("cuadrado", evaluator.evaluate(tokenizer.tokenize("(DEFUN cuadrado (x) (* x x))")));
    }

    @Test
    public void testCallCuadrado() {
        evaluator.evaluate(tokenizer.tokenize("(DEFUN cuadrado (x) (* x x))"));
        assertEquals(25.0, evaluator.evaluate(tokenizer.tokenize("(cuadrado 5)")));
    }

    @Test
    public void testDefunCubo() {
        assertEquals("cubo", evaluator.evaluate(tokenizer.tokenize("(DEFUN cubo (x) (* x x x))")));
    }

    @Test
    public void testCallCubo() {
        evaluator.evaluate(tokenizer.tokenize("(DEFUN cubo (x) (* x x x))"));
        assertEquals(27.0, evaluator.evaluate(tokenizer.tokenize("(cubo 3)")));
    }
}
