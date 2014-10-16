package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class IdProcessor extends SongResponseProcessor {

    public IdProcessor() {
        super("Id:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setId(Integer.parseInt(line.substring(getPrefix().length()).trim()));
        }
    }
}
