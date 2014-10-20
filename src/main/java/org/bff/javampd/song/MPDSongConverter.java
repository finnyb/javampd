package org.bff.javampd.song;


import org.bff.javampd.processor.SongProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author bill
 */
public class MPDSongConverter implements SongConverter {

    private static String PREFIX_FILE = SongProcessor.FILE.getProcessor().getPrefix();

    @Override
    public List<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<>();
        Iterator<String> iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(PREFIX_FILE))) {
                line = iterator.next();
            }

            if (line.startsWith(PREFIX_FILE)) {
                songList.add(processSong(line, iterator));
            }
        }
        return songList;
    }

    private MPDSong processSong(String line, Iterator<String> iterator) {
        MPDSong song = new MPDSong(line.substring(PREFIX_FILE.length()).trim());
        initialize(song);
        line = iterator.next();
        while (!line.startsWith(PREFIX_FILE)) {
            processLine(song, line);
            if (!iterator.hasNext()) {
                break;
            }
            line = iterator.next();
        }

        return song;
    }

    private void initialize(MPDSong song) {
        song.setName("");
        song.setAlbumName("");
        song.setArtistName("");
        song.setComment("");
        song.setDiscNumber("");
        song.setGenre("");
        song.setTitle("");
        song.setYear("");
    }

    @Override
    public List<String> getSongNameList(List<String> fileList) {
        String prefix = SongProcessor.FILE.getProcessor().getPrefix();
        List<String> names = new ArrayList<>();
        for (String s : fileList) {
            if (s.startsWith(prefix)) {
                names.add((s.substring(prefix.length())).trim());
            }
        }

        return names;
    }

    private static void processLine(MPDSong song, String line) {
        for (SongProcessor songProcessor : SongProcessor.values()) {
            songProcessor.getProcessor().processSong(song, line);
        }
    }
}
