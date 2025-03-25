package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorTest {

    private Map<String, Double> variableMap;

    @BeforeEach
    public void setup() {
        variableMap = new HashMap<>();
    }

    @Test
    public void testArithmeticOperationAdd() {
        List<Double> values = List.of(2.0, 3.0, 4.0);
        assertEquals(9.0, Operator.arithmeticOperation("+", values));
    }

    @Test
    public void testArithmeticOperationSubtract() {
        List<Double> values = List.of(10.0, 3.0);
        assertEquals(7.0, Operator.arithmeticOperation("-", values));
    }

    @Test
    public void testArithmeticOperationMultiply() {
        List<Double> values = List.of(2.0, 3.0, 4.0);
        assertEquals(24.0, Operator.arithmeticOperation("*", values));
    }

    @Test
    public void testArithmeticOperationDivide() {
        List<Double> values = List.of(20.0, 5.0);
        assertEquals(4.0, Operator.arithmeticOperation("/", values));
    }

    @Test
    public void testArithmeticOperationEmptyList() {
        List<Double> values = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> Operator.arithmeticOperation("+", values));
    }

    @Test
    public void testCompareValuesEqual() {
        assertEquals(1.0, Operator.compareValues("EQUAL", List.of("5", "5"), variableMap));
    }

    @Test
    public void testCompareValuesLessThan() {
        assertEquals(1.0, Operator.compareValues("<", List.of("3", "5"), variableMap));
    }

    @Test
    public void testCompareValuesGreaterThan() {
        assertEquals(1.0, Operator.compareValues(">", List.of("10", "5"), variableMap));
    }

    @Test
    public void testCompareValuesWithVariables() {
        variableMap.put("x", 8.0);
        variableMap.put("y", 8.0);
        assertEquals(1.0, Operator.compareValues("EQUAL", List.of("x", "y"), variableMap));
    }

    @Test
    public void testAssignVariable() {
        double result = Operator.assignVariable("a", 42.0, variableMap);
        assertEquals(42.0, result);
        assertEquals(42.0, variableMap.get("a"));
    }

    @Test
    public void testEvaluateCondTrueCondition() {
        variableMap.put("x", 10.0);
        List<List<String>> conditions = List.of(
            List.of(">", "x", "5", "x")
        );
        assertEquals(10.0, Operator.evaluateCond(conditions, variableMap));
    }

    @Test
    public void testEvaluateCondWithTCondition() {
        variableMap.put("z", 99.0);
        List<List<String>> conditions = List.of(
            List.of("T", "z")
        );
        assertEquals(99.0, Operator.evaluateCond(conditions, variableMap));
    }

    @Test
    public void testEvaluateCondNoTrueCondition() {
        variableMap.put("x", 1.0);
        List<List<String>> conditions = List.of(
            List.of(">", "x", "5", "x")
        );
        assertThrows(IllegalArgumentException.class, () -> Operator.evaluateCond(conditions, variableMap));
    }

    @Test
    public void testEvaluateCondInvalidConditionStructure() {
        List<List<String>> conditions = List.of(
            List.of("T") // Missing action
        );
        assertThrows(IllegalArgumentException.class, () -> Operator.evaluateCond(conditions, variableMap));
    }
}