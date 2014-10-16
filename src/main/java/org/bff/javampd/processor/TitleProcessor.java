package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class TitleProcessor extends SongResponseProcessor {

    public TitleProcessor() {
        super("Title:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setTitle(line.substring(getPrefix().length()).trim());
        }
    }
}
