package org.danielmueller.model;

public enum FileOutputTypeRegistry
{
    CONSOLE("console"),
    PLAIN_FILE("plain_file");

    private final String outputTypeString;

    FileOutputTypeRegistry(String outputTypeString)
    {
        this.outputTypeString = outputTypeString;
    }

    public String getOutputTypeString()
    {
        return outputTypeString;
    }
}
