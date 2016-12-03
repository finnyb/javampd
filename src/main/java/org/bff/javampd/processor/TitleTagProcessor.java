package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class TitleTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor {

    public TitleTagProcessor() {
        super("Title:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setTitle(line.substring(getPrefix().length()).trim());
        }
    }
}
