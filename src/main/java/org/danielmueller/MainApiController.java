package org.danielmueller;

import org.danielmueller.input.CommandLineData;
import org.danielmueller.input.CommandLineDataManager;
import org.danielmueller.input.FileDataReader;
import org.danielmueller.model.FileData;
import org.danielmueller.output.OutputManager;
import org.danielmueller.process.FileDataLoadSplitterBasic;
import org.danielmueller.process.FileDataSplittable;
import org.danielmueller.process.ProcessManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main implementation of application functionality interface
 *
 * @author Daniel Mueller
 */
public class MainApiController implements MainApi
{
    /**
     * so far, main entry point and main application functionality provided.
     *
     * @param args command line arguments in the form of -option[|--long_option] value[,value2]
     * @throws Exception generically specified various error potentially
     * occurring during invocation of present methods
     */
    @Override
    public void commandLineProcessFile(String[] args) throws Exception
    {
        CommandLineData commandLineData = CommandLineDataManager.processCommandLineArguments(args);

        FileData inputFileData = FileDataReader.readFile(commandLineData.getFilePath());

        // basic version - main-thread
        //FileData outputFileData = FileDataProcessor.processFileData(inputFileData, commandLineData);

        // extended version - multithreaded, invoking FileDataProcessor.processFileData()
        //  on each thread independently
        ProcessManager processManager = initializeParallelSetup(commandLineData);
        FileData outputFileData = processManager.parallelProcessFileData(inputFileData, commandLineData);

        OutputManager.outputData(outputFileData, commandLineData);

        // DO NOT CHANGE THE FOLLOWING LINES OF CODE
		System.out.printf("Processed %d lines (%d of which were unique)%n",
				Statistics.getInstance().getNoOfLinesRead(),
				Statistics.getInstance().getNoOfUniqueLines());
    }

    /**
     * In order to concurrently carry out the file data transformation task,
     * a pool of threads {@link java.util.concurrent.ExecutorService} needs
     * to be provided as well as a mechanism to split the data into batches,
     * each batch to be processed on a different (disjoint) thread (instance).
     *
     * As such, a {@link ProcessManager} is returned that is composed of an
     * ExecutorService as well as a mechanism to split to data according to
     * the number of threads specified within {@link CommandLineData}.
     *
     * @param commandLineData {@link CommandLineData}
     * @return a ProcessManager (singleton-)instance
     */
    private static ProcessManager initializeParallelSetup(CommandLineData commandLineData)
    {
        int nThreads = commandLineData.getNumberThreads();

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        FileDataSplittable fileDataSplitter = FileDataLoadSplitterBasic.getFileDataLoadSplitterBasicInstance(nThreads);

        return ProcessManager.createProcessManagerInstance(executorService, fileDataSplitter);
    }
}
