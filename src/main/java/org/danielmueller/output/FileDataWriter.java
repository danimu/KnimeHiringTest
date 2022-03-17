package org.danielmueller.output;

import org.danielmueller.model.FileData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class FileDataWriter
{
    private FileDataWriter()
    {

    }

    public static void writeFile(FileData outputFileData, String filePath)
    {
        boolean firstLine = true;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String line : outputFileData.getLines())
            {
                if (firstLine)
                {
                    firstLine = false;
                    writer.write(line);

                    continue;
                }

                writer.write(System.lineSeparator() + line);
            }

            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
