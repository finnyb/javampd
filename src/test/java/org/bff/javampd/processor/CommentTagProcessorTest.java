package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommentTagProcessorTest {

    @Test
    void testProcessSong() {
        String testComment = "testComment";

        CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "Comment:" + testComment;
        commentTagProcessor.processTag(song, line);

        assertEquals(testComment, song.getComment());
    }

    @Test
    void testProcessSongBadLine() {
        String testComment = "testComment";

        CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "BadComment:" + testComment;
        commentTagProcessor.processTag(song, line);

        assertNull(song.getComment());
    }
}
