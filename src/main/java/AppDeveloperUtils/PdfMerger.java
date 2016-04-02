package AppDeveloperUtils;

import com.google.common.annotations.VisibleForTesting;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Given a directory containing all and only pdf file(s)
 * when merge() is called
 * merges all files from the given directory into 1 single file saved into one level up to the given sourceDirectory
 *
 */
public class PdfMerger {

    private static class PdfCreator {
        String fileName;
        Document document;
        public PdfCopy copy;

        public PdfCreator(String fileName) {
            this.fileName = fileName;
        }

        public Document getDocument() {
            return document;
        }

        public PdfCreator invoke() throws DocumentException, FileNotFoundException {
            document = new Document();
            copy = new PdfCopy(document, new FileOutputStream(fileName));
            return this;
        }
    }

    public void merge() throws Exception {
        String outputPdfFileName = System.getProperty("codechilli.pdfmerger.combinedFileName");
        String soureDIrectoryPath = System.getProperty("codechilli.pdfmerger.sourceDirectoryPath");
        merge(soureDIrectoryPath, outputPdfFileName);

    }

    public void merge(String soureDIrectoryPath, String outputPdfFileName) throws Exception {
        String[] files = generateFileNames(soureDIrectoryPath);
        PdfCreator pdfCreator = new PdfCreator(soureDIrectoryPath + outputPdfFileName).invoke();
        Document combinedPDFDocument = pdfCreator.getDocument();
        PdfCopy copy = pdfCreator.copy;
        combinedPDFDocument.open();

        PdfReader ReadInputPDF;
        int number_of_pages;
        for (int i = 0; i < files.length; i++) {
            ReadInputPDF = new PdfReader(files[i]);
            number_of_pages = ReadInputPDF.getNumberOfPages();
            for (int page = 0; page < number_of_pages; ) {
                copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));
            }
        }
        combinedPDFDocument.close();
    }

    String[] generateFileNames(String sourceDirectoryPath) throws Exception {
        List<String> fileNames = new ArrayList<String>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(sourceDirectoryPath));
        for (Path path : directoryStream) {
            fileNames.add(path.toString());
        }
        Collections.sort(fileNames);
        return fileNames.toArray(new String[fileNames.size()]);
    }

    @Deprecated
    String[] generateFileNames(String sourceDirectoryPath, int start, int end) {
        String[] fileNames = new String[end];
        for (int i = start; i <= end; ++i) {
            fileNames[i - 1] = sourceDirectoryPath + i + ".pdf";
        }
        return fileNames;
    }

    public static void main(String[] args) {
        try {
            PdfMerger pdf = new PdfMerger();
            pdf.merge();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}