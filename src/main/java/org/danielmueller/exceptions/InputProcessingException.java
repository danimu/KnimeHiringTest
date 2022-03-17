package org.danielmueller.exceptions;

public class InputProcessingException extends RuntimeException
{
    public InputProcessingException(Exception exception)
    {
        super(exception);
    }
}
