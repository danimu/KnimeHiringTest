package org.danielmueller.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum FileDataInputTypeRegistry
{
    STRING("string"),
    INT("int"),
    DOUBLE("double");

    private final String inputTypeString;

    private static final Map<String, FileDataInputTypeRegistry> stringToEnumMap;

    static
    {
        Map<String, FileDataInputTypeRegistry> initializerMap = new HashMap<>();
        for (FileDataInputTypeRegistry dataType : FileDataInputTypeRegistry.values())
        {
            initializerMap.put(dataType.getString().toLowerCase(), dataType);
        }
        stringToEnumMap = Collections.unmodifiableMap(initializerMap);
    }

    FileDataInputTypeRegistry(String inputTypeString)
    {
        this.inputTypeString = inputTypeString;
    }

    public static FileDataInputTypeRegistry fromString(String inputTypeString)
    {
        return stringToEnumMap.get(inputTypeString);
    }

    public Class<?> getInputTypeClass()
    {
        switch (this)
        {
            case STRING:
                return String.class;
            case INT:
                return Integer.class;
            case DOUBLE:
                return Double.class;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public String getString()
    {
        return inputTypeString;
    }
}