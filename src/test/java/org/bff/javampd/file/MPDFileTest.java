package org.bff.javampd.file;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MPDFileTest {

    @Test
    public void testGetPath() throws Exception {
        String path = "/path/to/Name";
        MPDFile mpdFile = new MPDFile(path);
        mpdFile.setPath(path);
        assertEquals(path, mpdFile.getPath());
    }

    @Test
    public void testIsDirectory() throws Exception {
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