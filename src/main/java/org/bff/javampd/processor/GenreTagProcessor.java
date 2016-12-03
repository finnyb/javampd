package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public class GenreTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor {

    public GenreTagProcessor() {
        super("Genre:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setGenre(line.substring(getPrefix().length()).trim());
        }
    }
}
