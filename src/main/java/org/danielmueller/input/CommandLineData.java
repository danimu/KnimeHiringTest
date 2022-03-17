package org.danielmueller.input;

import org.apache.commons.cli.Option;
import org.danielmueller.model.DataOperationRegistry;
import org.danielmueller.model.FileOutputTypeRegistry;
import org.danielmueller.operations.DataOperable;
import org.danielmueller.model.FileDataInputTypeRegistry;
import org.danielmueller.model.FileOutputConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CommandLineData
{
    private final String filePath;
    private final FileDataInputTypeRegistry inputDataType;
    private final List<DataOperable> dataOperations;
    private final int numberThreads;
    private final FileOutputConfig outputConfig;

    private CommandLineData(final String filePath, final FileDataInputTypeRegistry inputDataTypeEnum,
                            final List<DataOperable> dataOperations, int numberThreads,
                            final FileOutputConfig outputConfig)
    {
        this.filePath = Objects.requireNonNull(filePath);
        this.inputDataType = Objects.requireNonNull(inputDataTypeEnum);
        this.dataOperations = Objects.requireNonNull(dataOperations);
        this.numberThreads = numberThreads;
        this.outputConfig = Objects.requireNonNull(outputConfig);
    }

    public static CommandLineData createCommandLineDataInstance(Map<Option, String> optionToValueMap)
    {
        final String filePath =
                optionToValueMap.get(
                        CommandLineOptionRegistry.retrieveCorrespondingCliOption(CommandLineOptionRegistry.INPUT)
                );
        final FileDataInputTypeRegistry inputDataType = FileDataInputTypeRegistry.fromString(
                optionToValueMap.get(
                        CommandLineOptionRegistry.retrieveCorrespondingCliOption(CommandLineOptionRegistry.INPUTTYPE)
                )
        );
        final List<DataOperable> transformationTypes = parseOperationTypes(
                optionToValueMap.get(
                        CommandLineOptionRegistry.retrieveCorrespondingCliOption(CommandLineOptionRegistry.OPERATIONS)
                )
        );
        final int nThreads = Integer.parseInt(
                optionToValueMap.get(
                        CommandLineOptionRegistry.retrieveCorrespondingCliOption(CommandLineOptionRegistry.THREADS)
                )
        );

        final FileOutputConfig outputConfig = assembleOutputConfig(optionToValueMap);

        return new CommandLineData(filePath, inputDataType, transformationTypes, nThreads, outputConfig);
    }

    private static FileOutputConfig assembleOutputConfig(Map<Option, String> optionToValueMap)
    {
        if (isOptionAvailable(optionToValueMap, CommandLineOptionRegistry.OUTPUT))
        {
            String outputPath = optionToValueMap.get(
                    CommandLineOptionRegistry.retrieveCorrespondingCliOption(CommandLineOptionRegistry.OUTPUT)
            );

            return new FileOutputConfig(FileOutputTypeRegistry.PLAIN_FILE, outputPath);
        }

        return new FileOutputConfig(FileOutputTypeRegistry.CONSOLE, FileOutputTypeRegistry.CONSOLE.getOutputTypeString());
    }

    private static boolean isOptionAvailable(Map<Option, String> optionToValueMap, CommandLineOptionRegistry outputType)
    {
        return Objects.nonNull(optionToValueMap.get(CommandLineOptionRegistry.retrieveCorrespondingCliOption(outputType)));
    }

    private static List<DataOperable> parseOperationTypes(String operationTypesString)
    {
        String[] operationTypes = operationTypesString.split(",");

        List<DataOperable> operationList = Arrays.stream(operationTypes)
                .map(DataOperationRegistry::getDataOperation)
                .collect(Collectors.toList());

        return Collections.unmodifiableList(operationList);
    }

    public String getFilePath()
    {
        return filePath;
    }

    public FileDataInputTypeRegistry getInputDataType()
    {
        return inputDataType;
    }

    public List<DataOperable> getDataOperations()
    {
        return dataOperations;
    }

    public int getNumberThreads()
    {
        return numberThreads;
    }

    public FileOutputTypeRegistry getOutputType()
    {
        return outputConfig.getOutputType();
    }

    public String getOutputPath()
    {
        return outputConfig.getOutputPath();
    }
}
