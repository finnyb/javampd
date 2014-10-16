package org.bff.javampd.file;

import org.bff.javampd.BaseTest;
import org.bff.javampd.TestProperties;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.integrationdata.TestFiles;
import org.bff.javampd.server.MPDConnectionException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDFileDatabaseIT extends BaseTest {
    private FileDatabase fileDatabase;
    private TestProperties testProperties;

    @Before
    public void setUp() throws Exception {
        fileDatabase = getMpd().getDatabaseManager().getFileDatabase();
        this.testProperties = TestProperties.getInstance();
    }

    @Test
    public void testListRootDirectory() throws IOException, MPDConnectionException, MPDDatabaseException {
        List<File> testFiles = new ArrayList<>(TestFiles.getRootTestFiles(testProperties.getPath()));
        List<MPDFile> files = new ArrayList<>(fileDatabase.listRootDirectory());

        assertEquals(testFiles.size(), files.size());

        for (File f : testFiles) {
            boolean found = false;
            for (MPDFile mpdF : fileDatabase.listRootDirectory()) {
                if (f.getName().equals(mpdF.getName())) {
                    found = true;
                    assertEquals(f.isDirectory(), mpdF.isDirectory());
                }
            }

            assertTrue(found);
        }
    }

    @Test
    public void testListDirectories() throws Exception {
        List<File> testFiles = new ArrayList<>(TestFiles.getRootTestFiles(testProperties.getPath()));

        for (File f : testFiles) {
            for (MPDFile mpdF : fileDatabase.listRootDirectory()) {
                if (f.getName().equals(mpdF.getName()) && f.isDirectory()) {
                    compareDirs(f, mpdF);
                }
            }
        }
    }

    @Test
    public void testListAllFiles() throws Exception {

    }

    @Test
    public void testListAllFiles1() throws Exception {

    }

    @Test
    public void testListAllSongFiles() throws Exception {

    }

    @Test
    public void testListAllSongFiles1() throws Exception {

    }

    private void compareDirs(File testFile, MPDFile file) throws Exception {
        List<File> testFiles = new ArrayList<>(TestFiles.getTestFiles(testFile));
        List<MPDFile> files = new ArrayList<>(fileDatabase.listDirectory(file));

        assertEquals(testFiles.size(), files.size());

        for (File f : testFiles) {
            boolean found = false;
            for (MPDFile mpdF : files) {
                if (f.getName().equals(mpdF.getName().replaceFirst(file.getName() + "/", ""))) {
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