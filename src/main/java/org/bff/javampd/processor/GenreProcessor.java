package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class GenreProcessor extends SongResponseProcessor {

    public GenreProcessor() {
        super("Genre:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setGenre(line.substring(getPrefix().length()).trim());
        }
    }
}
