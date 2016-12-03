package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;

public class AlbumTagProcessor extends TagResponseProcessor implements SongTagResponseProcessor, AlbumTagResponseProcessor {

    public AlbumTagProcessor() {
        super("Album:");
    }

    @Override
    public void processTag(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setAlbumName(line.substring(getPrefix().length()).trim());
        }
    }

    @Override
    public void processTag(MPDAlbum album, String line) {
        if (startsWith(line)) {
            album.setName(line.substring(getPrefix().length()).trim());
        }
    }
}
