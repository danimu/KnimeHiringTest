package org.danielmueller.exceptions;

public class FileDataProcessingException extends RuntimeException
{
    private final String line;

    public FileDataProcessingException()
    {
        super();
        this.line = "";
    }

    public FileDataProcessingException(final Exception e)
    {
        super(e);
        this.line = "";
    }

    public FileDataProcessingException(final String line, final Exception e)
    {
        super(e);
        this.line = line;
    }

    public FileDataProcessingException(final String line, final String additionalMessage)
    {
        super(additionalMessage);
        this.line = line;
    }

    public String getLine()
    {
        return this.line;
    }
}
