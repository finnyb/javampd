package org.bff.javampd.song;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author bill
 */
public class MPDSongConverter implements SongConverter {

    private static String delimitingPrefix = SongProcessor.getDelimitingPrefix();

    @Override
    public List<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<>();
        Iterator<String> iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(delimitingPrefix))) {
                line = iterator.next();
            }

            if (line.startsWith(delimitingPrefix)) {
                line = processSong(line.substring(delimitingPrefix.length()).trim(), iterator, songList);
            }
        }
        return songList;
    }

    private static String processSong(String file, Iterator<String> iterator, List<MPDSong> songs) {
        MPDSong song = new MPDSong(file, "");
        initialize(song);
        String line = iterator.next();
        while (!line.startsWith(delimitingPrefix)) {
            processLine(song, line);
            if (!iterator.hasNext()) {
                break;
            }
            line = iterator.next();
        }
        songs.add(song);

        return line;
    }

    private static void initialize(MPDSong song) {
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
    public List<String> getSongFileNameList(List<String> fileList) {
        String prefix = SongProcessor.FILE.getProcessor().getPrefix();

        List<String> list = new ArrayList<>();
        for (String s : fileList) {
            if (s.startsWith(prefix)) {
                String trim = (s.substring(prefix.length())).trim();
                list.add(trim);
            }
        }
        return list;
    }

    private static void processLine(MPDSong song, String line) {
        for (SongProcessor songProcessor : SongProcessor.values()) {
            songProcessor.getProcessor().processTag(song, line);
        }
    }
}
