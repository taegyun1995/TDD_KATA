package com.taegyun.tdd.string_calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Calculator {

    private static final String DEFAULT_DELIMITER = ",|\n";

    public int add(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        ParsedInput parsed = parse(input);
        String[] tokens = parsed.numbers().split(parsed.delimiter());

        validate(tokens);

        return sum(tokens);
    }

    private ParsedInput parse(String input) {
        if (input.startsWith("//")) {
            int newlineIndex = input.indexOf("\n");
            String delimiter = Pattern.quote(input.substring(2, newlineIndex));
            String numbers = input.substring(newlineIndex + 1);
            return new ParsedInput(delimiter, numbers);
        }
        return new ParsedInput(DEFAULT_DELIMITER, input);
    }

    private void validate(String[] tokens) {
        List<String> invalidValues = new ArrayList<>();
        List<Integer> negativeNumbers = new ArrayList<>();

        for (String token : tokens) {
            String trimmed = token.trim();

            try {
                int number = Integer.parseInt(trimmed);
                if (number < 0) {
                    negativeNumbers.add(number);
                }
            } catch (NumberFormatException e) {
                invalidValues.add(trimmed);
            }
        }

        if (!invalidValues.isEmpty()) {
            throw new RuntimeException("숫자가 아닌 값이 포함되어 있습니다: " + String.join(", ", invalidValues));
        }

        if (!negativeNumbers.isEmpty()) {
            String negativeStr = String.join(
                    ", ",
                    negativeNumbers.stream().map(String::valueOf).toList()
            );
            throw new RuntimeException("음수는 허용되지 않습니다: " + negativeStr);
        }
    }

    private int sum(String[] tokens) {
        int result = 0;
        for (String token : tokens) {
            int number = Integer.parseInt(token.trim());
            if (number <= 1000) {
                result += number;
            }
        }
        return result;
    }

}
