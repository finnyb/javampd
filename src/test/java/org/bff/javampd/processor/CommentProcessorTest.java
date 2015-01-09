package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testComment = "testComment";

        CommentProcessor commentProcessor = new CommentProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Comment:" + testComment;
        commentProcessor.processSong(song, line);

        assertEquals(testComment, song.getComment());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testComment = "testComment";

        CommentProcessor commentProcessor = new CommentProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadComment:" + testComment;
        commentProcessor.processSong(song, line);

        assertNull(song.getComment());
    }
}