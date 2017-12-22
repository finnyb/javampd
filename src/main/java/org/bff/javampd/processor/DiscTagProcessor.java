package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class DiscTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor {

    public DiscTagProcessor() {
        super("Disc:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setDiscNumber(line.substring(getPrefix().length()).trim());
        }
    }
}
