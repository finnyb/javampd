package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class CommentProcessor extends SongResponseProcessor {

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
