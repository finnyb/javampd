package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class CommentTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor {

    public CommentTagProcessor() {
        super("Comment:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setComment(line.substring(getPrefix().length()).trim());
        }
    }
}
