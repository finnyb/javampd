package org.bff.javampd.song;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * MPDSong represents a song in the MPD database that can be inserted into
 * a playlist.
 *
 * @author Bill
 * @version 1.0
 */
@SuperBuilder
@Data
public class MPDSong implements Comparable<MPDSong> {

    private final String name;
    private final String title;
    private final String albumArtist;
    private final String artistName;
    private final String albumName;

    /**
     * Returns the path of the song without a leading or trailing slash.
     */
    private final String file;
    private final String genre;
    private final String comment;
    private final String date;
    private final String discNumber;
    private final String track;

    /**
     * Returns the length of the song in seconds.
     */
    private final int length;

    /**
     * Returns the name of the song which can be different than the title if for example listening
     * to streaming radio.  If no name has been set then {@link #getTitle()} is used
     *
     * @return the name of the song if set, otherwise the title
     */
    public String getName() {
        if (this.name == null || "".equals(this.name)) {
            return getTitle();
        } else {
            return this.name;
        }
    }

    public int compareTo(MPDSong song) {
        StringBuilder sb;

        sb = new StringBuilder(getName());
        sb.append(getAlbumName() == null ? "" : getAlbumName());
        sb.append(getTrack());
        var thisSong = sb.toString();

        sb = new StringBuilder(song.getName());
        sb.append(song.getAlbumName() == null ? "" : song.getAlbumName());
        sb.append(getTrack());
        var songToCompare = sb.toString();

        return thisSong.compareTo(songToCompare);
    }
}
