package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class NameTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor {

    public NameTagProcessor() {
        super("Name:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setName(line.substring(getPrefix().length()).trim());
        }
    }
}
