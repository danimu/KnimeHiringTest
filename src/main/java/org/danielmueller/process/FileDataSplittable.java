package org.danielmueller.process;

import org.danielmueller.model.FileData;

import java.util.Map;

public interface FileDataSplittable
{
    Map<Integer, FileData> split(FileData t);
}
