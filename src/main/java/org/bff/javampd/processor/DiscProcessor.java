package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class DiscProcessor extends SongResponseProcessor {

    public DiscProcessor() {
        super("Disc:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setDiscNumber(line.substring(getPrefix().length()).trim());
        }
    }
}
