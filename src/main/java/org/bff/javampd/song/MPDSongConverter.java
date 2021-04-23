package org.bff.javampd.song;

import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.MPDItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MPDSongConverter implements SongConverter {

    private static final String DELIMITING_PREFIX = SongProcessor.getDelimitingPrefix();

    @Override
    public List<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<>();
        Iterator<String> iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(DELIMITING_PREFIX))) {
                line = iterator.next();
            }

            if (line.startsWith(DELIMITING_PREFIX)) {
                line = processSong(line.substring(DELIMITING_PREFIX.length()).trim(), iterator, songList);
            }
        }
        return songList;
    }

    private String processSong(String file, Iterator<String> iterator, List<MPDSong> songs) {
        MPDSong song = new MPDSong(file, "");
        initialize(song);
        String line = iterator.next();
        while (!line.startsWith(DELIMITING_PREFIX)) {
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
        return fileList.stream()
                .filter(s -> s.startsWith(DELIMITING_PREFIX))
                .map(s -> (s.substring(DELIMITING_PREFIX.length())).trim())
                .collect(Collectors.toList());
    }

    private void processLine(MPDItem song, String line) {
        SongProcessor songProcessor = SongProcessor.lookup(line);
        if (songProcessor != null) {
            songProcessor.getProcessor().processTag((MPDSong) song, line);
        } else {
            log.warn("Processor not found - {}", line);
        }
    }
}
