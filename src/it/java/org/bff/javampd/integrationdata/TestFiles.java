package org.bff.javampd.integrationdata;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestFiles {

    public static Collection<File> getTestFiles(File directory) {
        List<File> testFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files) {
            testFiles.add(file);
        }

        return testFiles;
    }

    /**
     * @return the testFiles
     */
    public static Collection<File> getRootTestFiles(String path) {
        List<File> testRootFiles = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (!file.getName().startsWith("ReadMe") && !file.isHidden()) {
                testRootFiles.add(file);
            }
        }

        return testRootFiles;
    }
}
