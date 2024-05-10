package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.service.CodeTunerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CodeTunerServiceImpl implements CodeTunerService {

    private static final Map<String, CodeTuner> tuners = Map.of(
            "Java", javaTuner()
    );

    @Override
    public String tune(String code, String language) {
        CodeTuner tuner = tuners.get(language);
        if (tuner != null) {
            return tuner.tune(code);
        }
        return code;
    }

    private static CodeTuner javaTuner(){
        return (String code) -> {
            Pattern pattern = Pattern.compile("import\\s+[^;]+;");
            Matcher matcher = pattern.matcher(code);
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                result.insert(0, matcher.group() + " ");
            }
            result.append(code.replaceAll("import\\s+[^;]+;", ""));
            return result.toString();
        };
    }

    @FunctionalInterface
    interface CodeTuner {
        String tune(String code);
    }
}
