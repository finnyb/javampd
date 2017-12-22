package org.bff.javampd.integrationdata;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestFiles {

    private static final List<String> IMAGE_SUFFIXES = Arrays.asList("jpg", "jpeg", "png");

    public static Collection<File> getTestFiles(File directory) {
        List<File> testFiles = new ArrayList<>();
        List<File> files = new ArrayList<>(Arrays.asList(directory.listFiles()));
        IMAGE_SUFFIXES.forEach(suffix -> files.removeIf(f -> f.getPath().endsWith(suffix)));

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
            if (!file.getName().startsWith("ReadMe") && !file.isHidden() && !file.getName().equals("playlists")) {
                testRootFiles.add(file);
            }
        }

        return testRootFiles;
    }
}
