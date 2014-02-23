package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentProcessor extends SongResponseProcessor {
    private static Logger logger = LoggerFactory.getLogger(CommentProcessor.class);

    public CommentProcessor() {
        super("Comment:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setComment(line.substring(getPrefix().length()).trim());
        }
    }
}
