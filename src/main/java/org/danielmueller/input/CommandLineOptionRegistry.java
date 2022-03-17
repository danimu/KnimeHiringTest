package org.danielmueller.input;

import org.apache.commons.cli.Option;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public enum CommandLineOptionRegistry
{
    INPUT("input"),
    INPUTTYPE("inputtype"),
    OPERATIONS("operations"),
    THREADS("threads"),
    OUTPUT("output");

    private static final Map<CommandLineOptionRegistry, Option> enumToCliOptionMap;

    static
    {
        EnumMap<CommandLineOptionRegistry, Option> enumTransferInitializerMap =
                new EnumMap<>(CommandLineOptionRegistry.class);
        enumTransferInitializerMap.put(INPUT,
                Option.builder("").longOpt(INPUT.getCliOptionString()).hasArg().required(true).build());
        enumTransferInitializerMap.put(INPUTTYPE,
                Option.builder("").longOpt(INPUTTYPE.getCliOptionString()).hasArg().required(true).build());
        enumTransferInitializerMap.put(OPERATIONS,
                Option.builder("").longOpt(OPERATIONS.getCliOptionString()).hasArgs().required(true).build());
        enumTransferInitializerMap.put(THREADS,
                Option.builder("").longOpt(THREADS.getCliOptionString()).hasArg().required(true).build());
        enumTransferInitializerMap.put(OUTPUT,
                Option.builder("").longOpt(OUTPUT.getCliOptionString()).hasArg().required(false).build());

        enumToCliOptionMap = Collections.unmodifiableMap(enumTransferInitializerMap);
    }

    private final String cliOptionString;

    CommandLineOptionRegistry(final String cliOptionString)
    {
        this.cliOptionString = cliOptionString;
    }

    public static Option retrieveCorrespondingCliOption(CommandLineOptionRegistry registryType)
    {
        return enumToCliOptionMap.get(registryType);
    }

    public static int numberOptions()
    {
        return CommandLineOptionRegistry.values().length;
    }

    public static List<Option> getRegistryTypeCorrespondingOptionsList()
    {
        return new LinkedList<>(enumToCliOptionMap.values());
    }

    public String getCliOptionString()
    {
        return this.cliOptionString;
    }
}
