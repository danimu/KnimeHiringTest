package org.danielmueller;

import org.danielmueller.exceptions.FileDataProcessingException;
import org.danielmueller.input.FileDataReader;
import org.danielmueller.model.FileData;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MainApiControllerTest
{
    @Test
    public void testFileDataTransformationsWorksForCorrectDataToOperationCombination()
    {
        String[] commandLineArgs = {
                "--input", "src/test/resources/input/input_data_positive.txt",
                "--inputtype", "int",
                "--operations", "neg,rev",
                "--threads", "3",
                "--output", "src/test/resources/output/output_data_positive_actual.txt"
        };

        MainApi controller = new MainApiController();

        try
        {
            controller.commandLineProcessFile(commandLineArgs);
        }
        catch (Exception e)
        {
            // don't need to do stuff here for current test
        }

        FileData actualLines = FileDataReader.readFile("src/test/resources/output/output_data_positive_actual.txt");
        FileData expectedLines = FileDataReader.readFile("src/test/resources/output/output_data_positive_expected.txt");

        Assertions.assertThat(expectedLines.getLines()).isEqualTo(actualLines.getLines());
        Assertions.assertThat(expectedLines.getUuid()).isNotEqualTo(actualLines.getUuid());
    }

    @Test(expected = FileDataProcessingException.class)
    public void testFileDataTransformationsThrowsDedicatedBatchIdError() throws Exception
    {
        String[] commandLineArgs = {
                "--input", "src/test/resources/input/input_data_error.txt",
                "--inputtype", "int",
                "--operations", "neg,rev",
                "--threads", "3"
        };

        MainApi controller = new MainApiController();

        controller.commandLineProcessFile(commandLineArgs);
    }
}

