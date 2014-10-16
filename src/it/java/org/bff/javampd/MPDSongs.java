package org.bff.javampd;

import org.bff.javampd.song.MPDSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Songs loaded from MPD
 */
public class MPDSongs {
    private static final List<MPDSong> songs = new ArrayList<>();

    public static List<MPDSong> getSongs() {
        return songs;
    }
}
