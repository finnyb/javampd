package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionProcessor extends SongResponseProcessor {
    private static Logger logger = LoggerFactory.getLogger(PositionProcessor.class);

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
