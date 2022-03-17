package org.danielmueller.process;

import org.danielmueller.input.CommandLineData;
import org.danielmueller.model.FileData;
import org.danielmueller.transform.FileDataProcessor;

import java.util.function.Supplier;

public final class FileDataProcessorSupplier implements Supplier<FileData>
{
    private final FileData inputFileData;
    private final CommandLineData commandLineData;

    private FileDataProcessorSupplier(final FileData inputFileData, final CommandLineData commandLineData)
    {
        this.inputFileData = inputFileData;
        this.commandLineData = commandLineData;
    }

    @Override
    public FileData get()
    {
        return FileDataProcessor.processFileData(inputFileData, commandLineData);
    }

    public static FileDataProcessorSupplier createFileDataProcessorSupplierInstance(final FileData inputFileData,
                                                                                    final CommandLineData commandLineData)
    {
        return new FileDataProcessorSupplier(inputFileData, commandLineData);
    }
}
