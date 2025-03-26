package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LispEvaluator {
    private final Map<String, Double> variables = new HashMap<>();
    private final Map<String, Object[]> funcionesDefinidas = new HashMap<>();

    public LispEvaluator(){}
    public LispEvaluator(Map<String, Double> variableMap) {
    }

    public double evaluate(List<String> expresion) {
        if (expresion.isEmpty()) {
            throw new IllegalArgumentException("Expresión vacía no válida");
        }

        if (expresion.get(0).equals("DEFUN")) {
            return evaluarDefun(expresion);
        }
        if (expresion.get(0).equals("PRINT")) {
            List<String> args = expresion.subList(1, expresion.size());
            try {
                double resultado = evaluate(args);
                System.out.println(resultado);
                return resultado;
            } catch (Exception e) {
                return Operator.evaluatePrint(args, variables);
            }
        }

        while (expresion.contains("(")) {
            int idxApertura = -1;
            int idxCierre = -1;
            int contadorParentesis = 0;

            for (int i = 0; i < expresion.size(); i++) {
                if (expresion.get(i).equals("(")) {
                    if (idxApertura == -1) {
                        idxApertura = i;
                    }
                    contadorParentesis++;
                }
                if (expresion.get(i).equals(")")) {
                    contadorParentesis--;
                    if (contadorParentesis == 0) {
                        idxCierre = i;
                        break;
                    }
                }
            }

            if (idxCierre == -1 || idxApertura > idxCierre) {
                throw new IllegalArgumentException("Error de paréntesis: No están balanceados o el orden es incorrecto.");
            }

            double resultado = evaluate(new ArrayList<>(expresion.subList(idxApertura + 1, idxCierre)));
            return resultado;
        }

        if (expresion.size() == 1 && isNumeric(expresion.get(0))) {
            return Double.parseDouble(expresion.get(0));
        }

        if (expresion.size() == 1) {
            if (expresion.get(0).equals("T")) return 1.0;
            if (expresion.get(0).equals("NIL")) return 0.0;
        }

        if (expresion.size() < 2) {
            throw new IllegalArgumentException("Error: Expresión incompleta o sin operandos.");
        }

        String oper = expresion.get(0);

        if (List.of("EQUAL", "<", ">").contains(oper)) {
            return Operator.compareValues(oper, expresion.subList(1, expresion.size()), variables);
        }

        if (oper.equals("SETQ")) {
            if (expresion.size() != 3) {
                throw new IllegalArgumentException("Error: SETQ requiere exactamente 2 argumentos.");
            }
            String variableName = expresion.get(1);
            double value = evaluate(List.of(expresion.get(2)));
            return Operator.assignVariable(variableName, value, variables);
        }

        if (oper.equals("COND")) {
            List<List<String>> condiciones = new ArrayList<>();
            int i = 1;
            while (i < expresion.size()) {
                if (expresion.get(i).equals("(")) {
                    int contadorParentesis = 1;
                    int j = i + 1;
                    while (j < expresion.size() && contadorParentesis > 0) {
                        if (expresion.get(j).equals("(")) contadorParentesis++;
                        else if (expresion.get(j).equals(")")) contadorParentesis--;
                        j++;
                    }
                    condiciones.add(expresion.subList(i + 1, j - 1));
                    i = j;
                } else {
                    i++;
                }
            }
            for (List<String> condicion : condiciones) {
                List<String> predicado = condicion.subList(0, condicion.size() - 1);
                String accion = condicion.get(condicion.size() - 1);

                List<String> evaluado = new ArrayList<>();
                evaluado.add("(");
                evaluado.addAll(predicado);
                evaluado.add(")");

                if (evaluate(evaluado) == 1.0) {
                    if (accion.equals("(")) {
                        return evaluate(condicion.subList(condicion.size() - 1, condicion.size()));
                    } else {
                        return evaluate(List.of(accion));
                    }
                }
            }
            throw new IllegalArgumentException("Error: Ninguna condición en COND se evaluó como verdadera.");
        }

        if (!List.of("+", "-", "*", "/").contains(oper) && !funcionesDefinidas.containsKey(oper)) {
            throw new IllegalArgumentException("Error: '" + oper + "' no es una operación válida en Lisp.");
        }

        if (funcionesDefinidas.containsKey(oper)) {
            Object[] def = funcionesDefinidas.get(oper);
            List<String> parametros = (List<String>) def[0];
            List<String> cuerpo = (List<String>) def[1];
            List<String> args = expresion.subList(1, expresion.size());

            if (args.size() != parametros.size()) {
                throw new IllegalArgumentException("Error: número de argumentos incorrecto para la función '" + oper + "'");
            }

            // Crear un entorno temporal de variables
            Map<String, Double> entornoTemporal = new HashMap<>(variables);
            for (int i = 0; i < args.size(); i++) {
                double valorArg = variables.containsKey(args.get(i)) || isNumeric(args.get(i))
                    ? evaluate(List.of(args.get(i)))
                    : Double.NaN;  // marcador simbólico en caso de error
                entornoTemporal.put(parametros.get(i), valorArg);
            }

            LispEvaluator subEvaluador = new LispEvaluator(entornoTemporal);
            subEvaluador.funcionesDefinidas.putAll(this.funcionesDefinidas);

            List<String> cuerpoEvaluado = new ArrayList<>();
            for (String token : cuerpo) {
                if (entornoTemporal.containsKey(token)) {
                    cuerpoEvaluado.add(entornoTemporal.get(token).toString());
                } else {
                    cuerpoEvaluado.add(token);
                }
            }
            return subEvaluador.evaluate(cuerpoEvaluado);
        }

        List<String> valoresStr = expresion.subList(1, expresion.size());
        return Operator.arithmeticOperation(oper, convertirValores(valoresStr));
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private List<Double> convertirValores(List<String> valoresStr) {
        List<Double> valores = new ArrayList<>();
        for (String val : valoresStr) {
            if (variables.containsKey(val)) {
                valores.add(variables.get(val));
            } else {
                try {
                    valores.add(Double.parseDouble(val));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error: '" + val + "' no está definido como variable en SETQ.");
                }
            }
        }
        return valores;
    }

    private double evaluarDefun(List<String> expresion) {
        if (expresion.size() < 4) {
            throw new IllegalArgumentException("Error: DEFUN requiere un nombre, lista de parámetros y cuerpo.");
        }

        String nombreFuncion = expresion.get(1);

        int idxParametrosInicio = -1;
        int idxParametrosFin = -1;
        int contadorParentesis = 0;

        for (int i = 2; i < expresion.size(); i++) {
            if (expresion.get(i).equals("(")) {
                if (idxParametrosInicio == -1) {
                    idxParametrosInicio = i;
                }
                contadorParentesis++;
            } else if (expresion.get(i).equals(")")) {
                contadorParentesis--;
                if (contadorParentesis == 0 && idxParametrosFin == -1) {
                    idxParametrosFin = i;
                    break;
                }
            }
        }

        if (idxParametrosInicio == -1 || idxParametrosFin == -1) {
            throw new IllegalArgumentException("Error: No se pudo identificar correctamente la lista de parámetros.");
        }

        List<String> parametros = new ArrayList<>();
        for (int i = idxParametrosInicio + 1; i < idxParametrosFin; i++) {
            if (!expresion.get(i).equals("(") && !expresion.get(i).equals(")")) {
                parametros.add(expresion.get(i));
            }
        }

        List<String> cuerpo = new ArrayList<>();
        for (int i = idxParametrosFin + 1; i < expresion.size(); i++) {
            if (!expresion.get(i).equals(")")) {
                cuerpo.add(expresion.get(i));
            } else {
                cuerpo.add(")");
                break;
            }
        }

        funcionesDefinidas.put(nombreFuncion, new Object[]{parametros, cuerpo});
        return 1.0;
    }
}