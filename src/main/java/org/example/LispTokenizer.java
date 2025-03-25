package org.example;
import java.util.ArrayList;
import java.util.List;

public class LispTokenizer implements ITokenizer {
    @Override
    public List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        String[] parts = input.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");

        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part);
            }
        }
        return tokens;
    }
}