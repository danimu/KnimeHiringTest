package org.danielmueller.model;

import org.danielmueller.operations.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum DataOperationRegistry
{
    CAPITALIZE("capitalize"),
    REVERSE("rev"),
    NEGATE("neg");

    private final String transformationTypeString;

    private static final Map<String, DataOperationRegistry> stringToEnumMap;
    private static final Map<DataOperationRegistry, DataOperable> dataOperationMap;

    static
    {
        Map<String, DataOperationRegistry> stringInitializerMap = new HashMap<>();
        for (DataOperationRegistry transformationType : DataOperationRegistry.values())
        {
            stringInitializerMap.put(transformationType.getString().toLowerCase(), transformationType);
        }

        stringToEnumMap = Collections.unmodifiableMap(stringInitializerMap);

        EnumMap<DataOperationRegistry, DataOperable> operationInitializerMap = new EnumMap<>(DataOperationRegistry.class);
        operationInitializerMap.put(CAPITALIZE, Capitalize.getInstance());
        operationInitializerMap.put(REVERSE, Reverse.getInstance());
        operationInitializerMap.put(NEGATE, Negate.getInstance());

        dataOperationMap = Collections.unmodifiableMap(operationInitializerMap);
    }

    DataOperationRegistry(String transformationTypeString)
    {
        this.transformationTypeString = transformationTypeString;
    }

    public static DataOperationRegistry fromString(String transformationType)
    {
        return stringToEnumMap.get(transformationType);
    }

    public static DataOperable getDataOperation(DataOperationRegistry operationEnum)
    {
        return dataOperationMap.get(operationEnum);
    }

    public static DataOperable getDataOperation(String operationString)
    {
        return dataOperationMap.get(stringToEnumMap.get(operationString));
    }

    public String getString()
    {
        return transformationTypeString;
    }
}
