package org.danielmueller;

/**
 * Application functionality interface to be implemented by main controlling classes.
 *
 * @author Daniel Mueller
 */
public interface MainApi
{
    void commandLineProcessFile(String[] args) throws Exception;
}
