package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class AlbumProcessor extends SongResponseProcessor {

    public AlbumProcessor() {
        super("Album:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setAlbumName(line.substring(getPrefix().length()).trim());
        }
    }
}
