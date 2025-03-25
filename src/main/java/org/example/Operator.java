package org.example;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Operator {
    public static double arithmeticOperation(String operation, List<Double> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Error: No operands provided for operation '" + operation + "'.");
        }

        double result = operation.equals("*") ? 1 : values.get(0);
        for (int i = (operation.equals("*") ? 0 : 1); i < values.size(); i++) {
            double value = values.get(i);
            switch (operation) {
                case "+": result += value; break;
                case "-": result -= value; break;
                case "*": result *= value; break;
                case "/": result /= value; break;
            }
        }
        return result;
    }

    public static double compareValues(String operation, List<String> values, Map<String, Double> variableMap) {
        if (values.size() != 2) {
            throw new IllegalArgumentException("Error: Comparison operations require exactly two operands.");
        }

        String v1 = values.get(0);
        String v2 = values.get(1);

        if (variableMap.containsKey(v1)) v1 = variableMap.get(v1).toString();
        if (variableMap.containsKey(v2)) v2 = variableMap.get(v2).toString();

        double val1 = Double.parseDouble(v1);
        double val2 = Double.parseDouble(v2);

        return switch (operation) {
            case "EQUAL" -> val1 == val2 ? 1.0 : 0.0;
            case "<" -> val1 < val2 ? 1.0 : 0.0;
            case ">" -> val1 > val2 ? 1.0 : 0.0;
            default -> throw new IllegalArgumentException("Error: Unsupported comparison operation '" + operation + "'.");
        };
    }

    public static double assignVariable(String variableName, double value, Map<String, Double> variableMap) {
        variableMap.put(variableName, value);
        return value;
    }

    public static double evaluateCond(List<List<String>> conditions, Map<String, Double> variableMap) {
        for (List<String> condition : conditions) {
            if (condition.size() < 2) {
                throw new IllegalArgumentException("Error: Cada condición en COND debe tener al menos dos elementos.");
            }

            // Extraer expresión condicional (todo excepto el último token)
            List<String> testExpr = condition.subList(0, condition.size() - 1);
            String action = condition.get(condition.size() - 1);

            boolean isTrue;

            // Caso especial T
            if (testExpr.size() == 1 && testExpr.get(0).equals("T")) {
                isTrue = true;
            } else {
                // Evaluar la expresión condicional simple
                String op = testExpr.get(0);
                List<String> operands = testExpr.subList(1, testExpr.size());

                // Reemplazar variables
                for (int i = 0; i < operands.size(); i++) {
                    if (variableMap.containsKey(operands.get(i))) {
                        operands.set(i, variableMap.get(operands.get(i)).toString());
                    }
                }

                isTrue = compareValues(op, operands, variableMap) == 1.0;
            }

            if (isTrue) {
                // Si es una variable, devolvemos su valor
                if (variableMap.containsKey(action)) {
                    return variableMap.get(action);
                }

                throw new IllegalArgumentException("Error: Acción inválida en COND: " + action);
            }
        }

        throw new IllegalArgumentException("Error: Ninguna condición en COND se evaluó como verdadera.");
    }
}