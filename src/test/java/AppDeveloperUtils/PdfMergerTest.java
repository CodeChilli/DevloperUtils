package AppDeveloperUtils;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by priya on 4/2/2016.
 */
public class PdfMergerTest {

    @Test
    public void merge() throws Exception {
        final String sourceDirectoryPath = "src/main/resources/sample";
        final String destination = "report";
        new PdfMerger().merge(sourceDirectoryPath,destination);
    }

    @Test
    public void generateFileNames_2() {
        PdfMerger pdf = new PdfMerger();
        final String sourceDirectoryPath = "resources\\sample";

        try {
            String[] fileNames = pdf.generateFileNames(sourceDirectoryPath);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed with exception");
        }

    }

}
