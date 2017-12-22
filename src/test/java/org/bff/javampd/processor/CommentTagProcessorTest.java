package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testComment = "testComment";

        CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Comment:" + testComment;
        commentTagProcessor.processTag(song, line);

        assertEquals(testComment, song.getComment());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testComment = "testComment";

        CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadComment:" + testComment;
        commentTagProcessor.processTag(song, line);

        assertNull(song.getComment());
    }
}