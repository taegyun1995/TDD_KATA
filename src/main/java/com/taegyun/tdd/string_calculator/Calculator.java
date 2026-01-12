package com.taegyun.tdd.string_calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 문자열 계산기
 * - 문자열로 전달된 숫자들의 합을 계산한다.
 */
public class Calculator {

    // 기본 구분자: 쉼표(,) 또는 줄바꿈(\n)
    private static final String DEFAULT_DELIMITER = ",|\n";

    private record ParsedInput(String delimiter, String numbers) {}

    public int add(String input) {
        // 1. 빈 문자열 또는 null 처리 → 0 반환
        if (input == null || input.isEmpty()) {
            return 0;
        }

        // 2~5. 구분자 파싱 (기본: 쉼표/줄바꿈, 커스텀: //구분자\n 형식)
        ParsedInput parsed = parse(input);
        String[] tokens = parsed.numbers().split(parsed.delimiter());

        // 6~7. 유효성 검증 (숫자가 아닌 값, 음수 예외 처리)
        validate(tokens);

        // 2~4, 8. 합산 (1000 초과 숫자 무시)
        return sum(tokens);
    }

    /**
     * 5. 커스텀 구분자 처리
     * - 문자열 시작이 "//"와 "\n" 사이의 문자를 커스텀 구분자로 사용
     * - 예: "//;\n1;2;3" → 구분자: ";", 숫자: "1;2;3"
     */
    private ParsedInput parse(String input) {
        if (input.startsWith("//")) {
            int newlineIndex = input.indexOf("\n");
            String delimiter = Pattern.quote(input.substring(2, newlineIndex));
            String numbers = input.substring(newlineIndex + 1);
            return new ParsedInput(delimiter, numbers);
        }
        return new ParsedInput(DEFAULT_DELIMITER, input);
    }

    /**
     * 6. 음수 예외 처리 - 음수가 포함되면 RuntimeException 발생
     * 7. 숫자가 아닌 값 예외 처리 - 숫자가 아닌 값이 포함되면 RuntimeException 발생
     */
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
            String negativeStr = String.join(", ",
                    negativeNumbers.stream().map(String::valueOf).toList());
            throw new RuntimeException("음수는 허용되지 않습니다: " + negativeStr);
        }
    }

    /**
     * 8. 1000 초과 숫자 무시
     * - 1000을 초과하는 숫자는 합산에서 제외
     * - 예: "2,1001" → 2 (1001은 무시)
     */
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
