package org.bff.javampd.file;

import org.bff.javampd.MPDException;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.DatabaseProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDFileDatabaseTest {

    @Mock
    private DatabaseProperties databaseProperties;
    @Mock
    private CommandExecutor commandExecutor;
    @InjectMocks
    private MPDFileDatabase fileDatabase;

    @Test
    public void testListRootDirectory() throws Exception {
        List<String> response = new ArrayList<>();
        response.add("directory: Q");
        response.add("Last-Modified: 2015-10-11T22:11:35Z");

        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo", "")).thenReturn(response);

        List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listRootDirectory());
        assertEquals(1, mpdFiles.size());
        assertEquals("Q", mpdFiles.get(0).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(0).getLastModified());
        assertTrue(mpdFiles.get(0).isDirectory());
    }

    @Test
    public void testListDirectory() throws Exception {
        String dir = "test";
        MPDFile file = new MPDFile(dir);
        file.setDirectory(true);
        fileDatabase.listDirectory(file);

        List<String> response = new ArrayList<>();
        response.add("directory: Q");
        response.add("Last-Modified: 2015-10-11T22:11:35Z");

        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo", dir)).thenReturn(response);

        List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
        assertEquals(1, mpdFiles.size());
        assertEquals("Q", mpdFiles.get(0).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(0).getLastModified());
        assertTrue(mpdFiles.get(0).isDirectory());
    }

    @Test
    public void testListDirectoryWithFiles() throws Exception {
        String dir = "test";
        MPDFile file = new MPDFile(dir);
        file.setDirectory(true);
        fileDatabase.listDirectory(file);

        List<String> response = new ArrayList<>();
        response.add("file: Q");
        response.add("Last-Modified: 2015-10-11T22:11:35Z");

        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo", dir)).thenReturn(response);

        List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
        assertEquals(1, mpdFiles.size());
        assertEquals("Q", mpdFiles.get(0).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(0).getLastModified());
        assertFalse(mpdFiles.get(0).isDirectory());
    }

    @Test
    public void testListDirectoryWithMultipleFiles() throws Exception {
        String dir = "test";
        MPDFile file = new MPDFile(dir);
        file.setDirectory(true);
        fileDatabase.listDirectory(file);

        List<String> response = new ArrayList<>();
        response.add("directory: Q");
        response.add("Last-Modified: 2015-10-11T22:11:35Z");
        response.add("file: R");
        response.add("Last-Modified: 2015-10-11T22:11:36Z");
        response.add("file: S");
        response.add("Last-Modified: 2015-10-11T22:11:37Z");
        response.add("directory: T");
        response.add("Last-Modified: 2015-10-11T22:11:38Z");

        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo", dir)).thenReturn(response);

        List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
        assertEquals(4, mpdFiles.size());
        assertEquals("Q", mpdFiles.get(0).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(0).getLastModified());
        assertTrue(mpdFiles.get(0).isDirectory());

        assertEquals("R", mpdFiles.get(1).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:36Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(1).getLastModified());
        assertFalse(mpdFiles.get(1).isDirectory());

        assertEquals("S", mpdFiles.get(2).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:37Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(2).getLastModified());
        assertFalse(mpdFiles.get(2).isDirectory());

        assertEquals("T", mpdFiles.get(3).getPath());
        assertEquals(LocalDateTime.parse("2015-10-11T22:11:38Z", DateTimeFormatter.ISO_DATE_TIME),
                mpdFiles.get(3).getLastModified());
        assertTrue(mpdFiles.get(3).isDirectory());
    }

    @Test(expected = MPDException.class)
    public void testListDirectoryException() throws Exception {
        MPDFile file = new MPDFile("");
        file.setDirectory(false);
        fileDatabase.listDirectory(file);
    }
}