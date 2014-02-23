package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class PositionProcessor extends SongResponseProcessor {

    public PositionProcessor() {
        super("Pos:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setPosition(Integer.parseInt(line.substring(getPrefix().length()).trim()));
        }
    }
}
