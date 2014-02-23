package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class ArtistProcessor extends SongResponseProcessor {

    public ArtistProcessor() {
        super("Artist:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setArtistName(line.substring(getPrefix().length()).trim());
        }
    }
}
