package org.danielmueller.transform;

import org.danielmueller.exceptions.FileDataProcessingException;
import org.danielmueller.input.CommandLineData;
import org.danielmueller.operations.DataOperable;
import org.danielmueller.model.FileData;

import java.util.List;
import java.util.stream.Collectors;

public final class FileDataProcessor
{
    private FileDataProcessor()
    {

    }

    public static FileData processFileData(final FileData fileData, final CommandLineData commandLineData)
    {
        Class<?> lineInputTypeClass = commandLineData.getInputDataType().getInputTypeClass();

        List<String> transformedLines = fileData.getLines().stream()
                .map(line -> processFileDataLine(line, commandLineData, lineInputTypeClass))
                .collect(Collectors.toList());

        return new FileData(transformedLines, fileData.getLineCount(), fileData.getUuid(), fileData.getBatchId());
    }

    public static String processFileDataLine(final String line, final CommandLineData commandLineData,
                                             final Class<?> lineInputTypeClass)
    {
        try
        {
            Object parsedLine = FileDataParser.getParsingFunction(lineInputTypeClass).apply(line);

            for (DataOperable operation : commandLineData.getDataOperations())
            {
                parsedLine = operation.dispatch(lineInputTypeClass, parsedLine);
            }

            return FileDataParser.getDeparsingFunction(lineInputTypeClass).apply(parsedLine);
        }
        catch (Exception e)
        {
            throw new FileDataProcessingException(line, e);
        }
    }
}
