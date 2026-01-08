package com.taegyun.tdd.string_calculator;

public class Calculator {

    public int add(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(text);
    }

}
