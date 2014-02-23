package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class TimeProcessor extends SongResponseProcessor {

    public TimeProcessor() {
        super("Time:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setLength(Integer.parseInt(line.substring(getPrefix().length()).trim()));
        }
    }
}
