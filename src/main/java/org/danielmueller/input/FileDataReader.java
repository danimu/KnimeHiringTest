package org.danielmueller.input;

import org.danielmueller.Statistics;
import org.danielmueller.model.FileData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class FileDataReader
{
    private FileDataReader()
    {
    }

    public static FileData readFile(final String filePath)
    {
        List<String> textFile = new LinkedList<>();
        long lineCount = 0L;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;

            while ((line = br.readLine()) != null)
            {
                textFile.add(line);
                lineCount++;

                Statistics.getInstance().updateStatisticsWithLine(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new FileData(textFile, lineCount);
    }
}