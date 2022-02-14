package org.bff.javampd.song;

import java.util.List;

/**
 * @author bill
 */
public interface SongConverter {
    /**
     * Converts the response from the MPD server into a {@link MPDSong} object.
     *
     * @param list the response from the MPD server
     * @return a MPDSong object
     */
    List<MPDSong> convertResponseToSongs(List<String> list);

    List<String> getSongFileNameList(List<String> fileList);
}
