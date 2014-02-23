package org.bff.javampd.processor;

import org.bff.javampd.objects.MPDSong;

public class FileProcessor extends SongResponseProcessor {

    public FileProcessor() {
        super("file:");
    }

    @Override
    public void processSong(MPDSong song, String line) {
        if (startsWith(line)) {
            song.setFile(line.substring(getPrefix().length()).trim());
        }
    }
}
