package org.danielmueller.process;

import org.danielmueller.model.FileData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class FileDataLoadSplitterBasic implements FileDataSplittable
{
    private static FileDataLoadSplitterBasic instance;

    private final int nChunks;

    private FileDataLoadSplitterBasic(int nChunks)
    {
        this.nChunks = nChunks;
    }

    @Override
    public Map<Integer, FileData> split(FileData fileData)
    {
        return splitIntoEquiSizedConsecutiveBatches(fileData);
    }

    private Map<Integer, FileData> splitIntoEquiSizedConsecutiveBatches(FileData fileData)
    {
        Map<Integer, FileData> chunkIdToFileDataSubsetMap = new HashMap<>();

        final long chunkSize = determineBatchSize(fileData);

        System.out.println("Splitting FileData into " + nChunks + " batches of size " + chunkSize + " each (last may differ)");
        for (int i = 0; i < nChunks; i++)
        {
            long from = i * chunkSize;
            long to = (i + 1) * chunkSize;  // list.sublist() takes 'to' as 'to'-exclusive

            if ((i + 1) == nChunks)
            {
                to = Math.min(to, fileData.getLineCount());
            }

            UUID batchUuid = UUID.randomUUID();
            FileData fileDataSubset =
                    new FileData(fileData.getLines(from, to), Math.subtractExact(to, from), batchUuid, i);

            chunkIdToFileDataSubsetMap.put(i, fileDataSubset);

            System.out.println("Initializing batch=" + i + " | uuid=" + batchUuid + " | lines=" + (from + 1) + "-" + to);
        }

        return chunkIdToFileDataSubsetMap;
    }

    private int determineBatchSize(FileData fileData)
    {
        return (int) Math.ceil((double) fileData.getLineCount() / nChunks);
    }

    public static FileDataLoadSplitterBasic getFileDataLoadSplitterBasicInstance(int nChunks)
    {
        if (Objects.isNull(instance))
        {
            instance = new FileDataLoadSplitterBasic(nChunks);
        }

        return instance;
    }
}
