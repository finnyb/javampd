package org.bff.javampd;


import org.bff.javampd.objects.MPDSong;
import org.bff.javampd.processor.SongProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author bill
 */
public class MPDSongConverter {

    private static String PREFIX_FILE = SongProcessor.FILE.getPrefix().getPrefix();

    private MPDSongConverter() {
    }

    /**
     * Converts the response from the MPD server into a {@link org.bff.javampd.objects.MPDSong} object.
     *
     * @param list the response from the MPD server
     * @return a MPDSong object
     */
    public static List<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<>();
        Iterator<String> iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(PREFIX_FILE))) {
                line = iterator.next();
            }

            if (line.startsWith(PREFIX_FILE)) {
                MPDSong song = new MPDSong();
                song.setFile(line.substring(PREFIX_FILE.length()).trim());
                line = iterator.next();
                while (!line.startsWith(PREFIX_FILE)) {
                    processLine(song, line);
                    if (!iterator.hasNext()) {
                        break;
                    }
                    line = iterator.next();
                }
                songList.add(song);
            }
        }
        return songList;
    }

    private static void processLine(MPDSong song, String line) {
        for (SongProcessor songProcessor : SongProcessor.values()) {
            songProcessor.getPrefix().processSong(song, line);
        }
    }

    public static List<String> getSongNameList(List<String> fileList) {
        List<String> names = new ArrayList<>();
        for (String s : fileList) {
            if (s.startsWith(PREFIX_FILE)) {
                names.add((s.substring(PREFIX_FILE.length())).trim());
            }
        }

        return names;
    }
}
