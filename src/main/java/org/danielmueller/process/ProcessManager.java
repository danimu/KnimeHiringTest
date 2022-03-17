package org.danielmueller.process;

import org.danielmueller.exceptions.FileDataProcessingException;
import org.danielmueller.input.CommandLineData;
import org.danielmueller.model.FileData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ProcessManager
{
    private final ExecutorService executorService;
    private final FileDataSplittable fileDataSplitter;

    private ProcessManager(final ExecutorService executorService, final FileDataSplittable loadSplitter)
    {
        this.executorService = executorService;
        this.fileDataSplitter = loadSplitter;
    }

    public FileData parallelProcessFileData(final FileData fileData, final CommandLineData commandLineData)
            throws InterruptedException, ExecutionException
    {
        Map<Integer, FileData> batchedFileDataMap = splitDataIntoBatches(fileData);

        Map<Integer, Supplier<FileData>> runnableSupplierMap = createFileDataProcessorSupplier(
                batchedFileDataMap, commandLineData);

        Map<Integer, Future<FileData>> futures = invokeThreads(runnableSupplierMap, batchedFileDataMap);

        List<FileData> processedFileDataList = retrieveProcessingResults(futures);

        processCleanUp();

        return combineBatchesToSingleFileDataObject(processedFileDataList);
    }

    private Map<Integer, FileData> splitDataIntoBatches(final FileData inputFileData)
    {
        return fileDataSplitter.split(inputFileData);
    }

    private Map<Integer, Supplier<FileData>> createFileDataProcessorSupplier(
            final Map<Integer, FileData> batchedFileDataMap, final CommandLineData commandLineData)
    {
        Map<Integer, Supplier<FileData>> threadsMap = new HashMap<>();
        for (Map.Entry<Integer, FileData> fileDataEntry : batchedFileDataMap.entrySet())
        {
            threadsMap.put(
                    fileDataEntry.getKey(),
                    FileDataProcessorSupplier.createFileDataProcessorSupplierInstance(
                            fileDataEntry.getValue(), commandLineData
                    )
            );
        }

        return threadsMap;
    }

    private Map<Integer, Future<FileData>> invokeThreads(final Map<Integer, Supplier<FileData>> callableThreadsMap,
                                                         final Map<Integer, FileData> originalFileDataMap)
    {
        Map<Integer, Future<FileData>> futures = new HashMap<>();

        for (Map.Entry<Integer, Supplier<FileData>> fileDataSupplierEntry : callableThreadsMap.entrySet())
        {
            FileData originalFileData = originalFileDataMap.get(fileDataSupplierEntry.getKey());

            futures.put(
                    fileDataSupplierEntry.getKey(),
                    CompletableFuture
                            .supplyAsync(fileDataSupplierEntry.getValue(), executorService)
                            .exceptionally(originalFileData::handleException)
            );
        }

        return futures;
    }

    private List<FileData> retrieveProcessingResults(final Map<Integer, Future<FileData>> futuresMap)
            throws InterruptedException, ExecutionException
    {
        List<FileData> resultingFileData = new LinkedList<>();

        for (Map.Entry<Integer, Future<FileData>> futureEntry : futuresMap.entrySet())
        {
            FileData fileData = futureEntry.getValue().get();

            if (fileData.isEmpty())
            {
                String errorMsg = "Batch Id=" + futureEntry.getKey() + " corrupt with no data! Stop Routine!";

                throw new FileDataProcessingException("", errorMsg);
            }

            resultingFileData.add(fileData);
        }

        return resultingFileData;
    }

    private void processCleanUp() throws InterruptedException
    {
        executorService.shutdown();

        executorService.awaitTermination(10_000L, TimeUnit.MILLISECONDS);
    }

    private FileData combineBatchesToSingleFileDataObject(List<FileData> processedFileDataList)
    {
        List<String> combinedLines = processedFileDataList.stream()
                .sorted()
                .map(FileData::getLines)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new FileData(combinedLines);
    }

    public static ProcessManager createProcessManagerInstance(final ExecutorService executorService,
                                                              final FileDataSplittable loadBalancer)
    {
        return new ProcessManager(executorService, loadBalancer);
    }
}
