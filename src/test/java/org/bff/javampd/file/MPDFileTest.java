package org.bff.javampd.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MPDFileTest {

    @Test
    public void testGetPath() {
        String path = "/path/to/Name";
        MPDFile mpdFile = new MPDFile(path);
        mpdFile.setPath(path);
        assertEquals(path, mpdFile.getPath());
    }

    @Test
    public void testIsDirectory() {
        MPDFile mpdFile = new MPDFile("");
        mpdFile.setDirectory(false);
        assertFalse(mpdFile.isDirectory());
    }

    @Test
    public void testToString() {
        String path = "/path/to/Name";
        MPDFile mpdFile = new MPDFile(path);
        mpdFile.setPath(path);
        assertEquals(path, mpdFile.toString());
    }
}