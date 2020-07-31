package org.bff.javampd.file;

import org.bff.javampd.BaseTest;
import org.bff.javampd.TestProperties;
import org.bff.javampd.integrationdata.TestFiles;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MPDFileDatabaseIT extends BaseTest {
    private FileDatabase fileDatabase;
    private TestProperties testProperties;

    @BeforeEach
    public void setUp() throws Exception {
        fileDatabase = getMpd().getMusicDatabase().getFileDatabase();
        this.testProperties = TestProperties.getInstance();
    }

    @Test
    public void testListRootDirectory() {
        List<File> testFiles = new ArrayList<>(TestFiles.getRootTestFiles(testProperties.getPath()));
        List<MPDFile> files = new ArrayList<>(fileDatabase.listRootDirectory());

        assertEquals(testFiles.size(), files.size());

        for (File f : testFiles) {
            boolean found = false;
            for (MPDFile mpdF : fileDatabase.listRootDirectory()) {
                if (f.getName().equals(mpdF.getPath())) {
                    found = true;
                    assertEquals(f.isDirectory(), mpdF.isDirectory());
                }
            }

            assertTrue(found);
        }
    }

    @Test
    public void testListDirectories() {
        List<File> testFiles = new ArrayList<>(TestFiles.getRootTestFiles(testProperties.getPath()));

        for (File f : testFiles) {
            boolean found = false;
            for (MPDFile mpdF : fileDatabase.listRootDirectory()) {
                if (f.getName().equals(mpdF.getPath()) && f.isDirectory()) {
                    found = true;
                    compareDirs(f, mpdF);
                }
            }
            assertTrue(found);
        }
    }

    private void compareDirs(File testFile, MPDFile file) {
        List<File> testFiles = new ArrayList<>(TestFiles.getTestFiles(testFile));
        List<MPDFile> files = new ArrayList<>(fileDatabase.listDirectory(file));

        assertEquals(testFiles.size(), files.size());

        for (File f : testFiles) {
            boolean found = false;
            for (MPDFile mpdF : files) {
                if (f.getName().equals(mpdF.getPath().replaceFirst(file.getPath() + "/", ""))) {
                    found = true;
                    assertEquals(f.isDirectory(), mpdF.isDirectory());
                    if (f.isDirectory()) {
                        compareDirs(f, mpdF);
                    }
                }
            }

            assertTrue(found);
        }
    }
}