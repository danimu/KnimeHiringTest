package org.danielmueller.input;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class providing methods to process command line arguments. Wraps
 * functionality of Apache Common's CLI package
 *
 * @author Daniel Mueller
 *
 * @see CommandLine
 * @see DefaultParser
 * @see Options
 */
public final class CommandLineDataManager
{
    private CommandLineDataManager()
    {
    }

    /**
     * Take command line arguments and map onto an internal representation
     * {@link CommandLineData}. This class frees the implementation from
     * any further passing along of Strings, attempting to ensure type
     * safety further.
     *
     * @param args command line arguments as passed by main-method
     * @return {@link CommandLineData}
     * @throws ParseException apache common's inherent exception class
     */
    public static CommandLineData processCommandLineArguments(String[] args) throws ParseException
    {
        Map<Option, String> optionToValueMap = parseCommandLineArguments(args);

        return CommandLineData.createCommandLineDataInstance(optionToValueMap);
    }

    private static Map<Option, String> parseCommandLineArguments(String[] args) throws ParseException
    {
        Options cliOptions = createOptionsInstance();

        DefaultParser defaultParser = new DefaultParser();
        CommandLine commandLine = defaultParser.parse(cliOptions, args);

        return CommandLineOptionRegistry.getRegistryTypeCorrespondingOptionsList()
                .stream()
                .filter(commandLine::hasOption)
                .collect(Collectors.toMap(Function.identity(), commandLine::getOptionValue));
    }

    /**
     * Referring to {@link CommandLineOptionRegistry} which provides a mapping of
     * command line arguments onto Apache common's CLI {@link Option}'s, an
     * {@link Options}-instance is created containing all options which the
     * application is able to process, no matter if required or not.
     *
     * @return {@link Options}
     */
    public static Options createOptionsInstance()
    {
        Options cliOptions = new Options();

        Arrays.stream(CommandLineOptionRegistry.values())
                .forEach(registryType -> cliOptions.addOption(
                        CommandLineOptionRegistry.retrieveCorrespondingCliOption(registryType)
                ));

        return cliOptions;
    }
}
