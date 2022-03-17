package org.danielmueller.model;

import org.danielmueller.exceptions.FileDataProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class FileData implements Comparable<FileData>
{
    private final List<String> lines;
    private final long lineCount;
    private final UUID uuid;
    private final long batchId;

    public FileData(List<String> lines)
    {
        this(lines, lines.toArray().length);
    }

    public FileData(List<String> lines, long lineCount)
    {
        this(lines, lineCount, UUID.randomUUID());
    }

    public FileData(List<String> lines, long lineCount, UUID uuid)
    {
        this(lines, lineCount, uuid, -1L);
    }

    public FileData(List<String> lines, long lineCount, UUID uuid, long batchId)
    {
        this.lines = lines;
        this.lineCount = lineCount;
        this.uuid = uuid;
        this.batchId = batchId;
    }

    public List<String> getLines()
    {
        return lines;
    }

    public List<String> getLines(final long from, final long to)
    {
        // if int does not suffice, a different method is needed - good for now
        return lines.subList((int) from, (int) to);
    }

    public long getLineCount()
    {
        return lineCount;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public long getBatchId()
    {
        return batchId;
    }

    public boolean isEmpty()
    {
        return this.lines.isEmpty() && this.lineCount == 0L;
    }

    public FileData handleException(Throwable throwable)
    {
        if (throwable instanceof FileDataProcessingException)
        {
            FileDataProcessingException fileDataProcessingException = (FileDataProcessingException) throwable;

            System.out.println("[FileDataProcessingException] file data line=" + fileDataProcessingException.getLine() +
                    " | batch ID=" + this.batchId + " | file data uuid=" + this.uuid);
        }

        throwable.printStackTrace();

        return new FileData(new ArrayList<>(0), 0L, this.getUuid(), this.getBatchId());
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof FileData))
        {
            return false;
        }

        FileData otherFileData = (FileData) other;

        return Objects.equals(this.uuid, otherFileData.getUuid()) &&
                Objects.equals(this.lines, otherFileData.getLines());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, lines);
    }

    @Override
    public int compareTo(FileData other)
    {
        return Long.compare(this.batchId, other.getBatchId());
    }
}
