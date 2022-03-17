package org.danielmueller.output;

import org.danielmueller.input.CommandLineData;
import org.danielmueller.model.FileData;

public final class OutputManager
{
    private OutputManager()
    {

    }

    public static void outputData(FileData fileData, CommandLineData commandLineData)
    {
        switch (commandLineData.getOutputType())
        {
            case CONSOLE:
                printOutputToConsole(fileData);
                break;
            case PLAIN_FILE:
                FileDataWriter.writeFile(fileData, commandLineData.getOutputPath());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static void printOutputToConsole(FileData fileData)
    {
        fileData.getLines().forEach(System.out::println);
    }
}
