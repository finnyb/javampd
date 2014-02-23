package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class DateProcessor extends SongResponseProcessor {

    public DateProcessor() {
        super("Date:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setYear(line.substring(getPrefix().length()).trim());
        }
    }
}
