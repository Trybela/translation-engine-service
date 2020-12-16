package com.avenga.fil.lt.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
public class TranslateFunction implements Function<String, String> {

    @Override
    public String apply(String s) {
        return "success result";
    }
}
