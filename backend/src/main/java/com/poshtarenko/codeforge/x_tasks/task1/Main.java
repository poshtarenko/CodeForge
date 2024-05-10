package com.poshtarenko.codeforge.x_tasks.task1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String input = "some text import xyz ; some more text import abc ;";
        Pattern pattern = Pattern.compile("import\\s+[^;]+;");
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.insert(0, matcher.group() + " ");
        }
        result.append(input.replaceAll("import\\s+[^;]+;", ""));
        System.out.println("Результат: " + result.toString());
    }
}

