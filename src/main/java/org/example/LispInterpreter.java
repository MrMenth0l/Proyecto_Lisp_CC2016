package org.example;


public class LispInterpreter {
    public static void main(String[] args) {
        ITokenizer tokenizer = new LispTokenizer();
        LispEvaluator evaluator = new LispEvaluator();

        String expr1 = "(+ 5 (* 2 3))";
        System.out.println("Resultado: " + evaluator.evaluate(tokenizer.tokenize(expr1)));

        String expr2 = "(+ 3 4 (* 2 3 ) 5 )";
        System.out.println("Resultado " + evaluator.evaluate(tokenizer.tokenize(expr2)));

        String expr3 = "(SETQ X 10)";
        System.out.println("Asignación SETQ: " + evaluator.evaluate(tokenizer.tokenize(expr3)));

        String expr4 = "(+ X 5)";
        System.out.println("Uso de variable X: " + evaluator.evaluate(tokenizer.tokenize(expr4)));

        String expr5 = "(EQUAL 10 10)";
        System.out.println("Comparación EQUAL (10 == 10): " + evaluator.evaluate(tokenizer.tokenize(expr5)));

        String expr6 = "(< 5 10)";
        System.out.println("Comparación menor que (5 < 10): " + evaluator.evaluate(tokenizer.tokenize(expr6)));

        String expr7 = "(> 15 7)";
        System.out.println("Comparación mayor que (15 > 7): " + evaluator.evaluate(tokenizer.tokenize(expr7)));

        String expr8 = "(COND ((> 10 5) 100) ((< 2 3) 200) (T 300))";
        System.out.println("Resultado COND 1: " + evaluator.evaluate(tokenizer.tokenize(expr8)));

        String expr9 = "(SETQ Y 8)";
        System.out.println("Asignación SETQ Y: " + evaluator.evaluate(tokenizer.tokenize(expr9)));

        String expr10 = "(COND ((< Y 5) 50) ((> Y 6) 60) (T 70))";
        System.out.println("Resultado COND 2: " + evaluator.evaluate(tokenizer.tokenize(expr10)));

        String expr11 = "(COND ((< Y 5) (+ 2 3)) ((> Y 6) (* 2 3)) (T (- 10 3)))";
        System.out.println("Resultado COND 3 (acciones como expresiones): " + evaluator.evaluate(tokenizer.tokenize(expr11)));

        String expr12 = "(COND ((T) 999))";
        System.out.println("Resultado COND 4 (solo T): " + evaluator.evaluate(tokenizer.tokenize(expr12)));

        String expr13 = "(DEFUN cuadrado (x) (* x x))";
        System.out.println("Definición de función cuadrado: " + evaluator.evaluate(tokenizer.tokenize(expr13)));

        String expr14 = "(cuadrado 5)";
        System.out.println("Resultado de (cuadrado 5): " + evaluator.evaluate(tokenizer.tokenize(expr14)));

        String expr15 = "(DEFUN cubo (x) (* x x x))";
        System.out.println("Definición de función cubo: " + evaluator.evaluate(tokenizer.tokenize(expr15)));

        String expr16 = "(cubo 3)";
        System.out.println("Resultado de (cubo 3):" + evaluator.evaluate(tokenizer.tokenize(expr16)));

    }
}
