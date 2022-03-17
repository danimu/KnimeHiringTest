package org.danielmueller.transform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class FileDataParser
{
    private static final Map<Class<?>, Function<String, ?>> classToParsingFunction;
    private static final Map<Class<?>, Function<?, String>> classToDeparsingFunction;

    static
    {
        Map<Class<?>, Function<String, ?>> parseInitializerMap = new HashMap<>();
        parseInitializerMap.put(Integer.class, Integer::parseInt);
        parseInitializerMap.put(Double.class, Double::parseDouble);
        parseInitializerMap.put(String.class, Function.identity());

        classToParsingFunction = Collections.unmodifiableMap(parseInitializerMap);

        Map<Class<?>, Function<Object, String>> deparseInitializerMap = new HashMap<>();
        deparseInitializerMap.put(Integer.class, String::valueOf);
        deparseInitializerMap.put(Double.class, String::valueOf);
        deparseInitializerMap.put(String.class, String.class::cast);

        classToDeparsingFunction = Collections.unmodifiableMap(deparseInitializerMap);
    }

    private FileDataParser()
    {

    }

    public static Function<String, ?> getParsingFunction(Class<?> clazz)
    {
        return classToParsingFunction.get(clazz);
    }

    public static Function<Object, String> getDeparsingFunction(Class<?> clazz)
    {
        return (Function<Object, String>) classToDeparsingFunction.get(clazz);
    }
}
