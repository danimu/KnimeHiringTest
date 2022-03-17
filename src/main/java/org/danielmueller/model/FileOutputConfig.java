package org.danielmueller.model;

public final class FileOutputConfig
{
    private final FileOutputTypeRegistry outputType;
    private final String outputPath;

    public FileOutputConfig(FileOutputTypeRegistry outputType, String outputPath)
    {
        this.outputType = outputType;
        this.outputPath = outputPath;
    }

    public FileOutputTypeRegistry getOutputType()
    {
        return outputType;
    }

    public String getOutputPath()
    {
        return outputPath;
    }
}
