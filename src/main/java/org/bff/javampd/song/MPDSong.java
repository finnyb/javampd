package org.bff.javampd.song;

import lombok.*;

/**
 * MPDSong represents a song in the MPD database that can be inserted into
 * a playlist.
 *
 * @author Bill
 * @version 1.0
 */
@Builder
@Data
public class MPDSong implements Comparable<MPDSong>{

    private String name;
    private String title;
    private String artistName;
    private String albumName;

    /**
     * Returns the path of the song without a leading or trailing slash.
     *
     * @return the path of the song
     */
    private String file;
    private String genre;
    private String comment;
    private String year;
    private String discNumber;

    /**
     * Returns the length of the song in seconds.
     *
     * @return the length of the song
     */
    private int length;
    private int track;

    /**
     * Returns the position of the song in the playlist. Returns
     * a -1 if the song is not in the playlist.
     *
     * @return the position in the playlist
     */
    @Builder.Default
    private int position = -1;

    /**
     * Returns the playlist song id for the song. Returns
     * a -1 if the song is not in the playlist.
     *
     * @return song id of the song
     */
    @Builder.Default
    private int id = -1;

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
        sb.append(formatToComparableString(getTrack()));
        var thisSong = sb.toString();

        sb = new StringBuilder(song.getName());
        sb.append(song.getAlbumName() == null ? "" : song.getAlbumName());
        sb.append(formatToComparableString(song.getTrack()));
        var songToCompare = sb.toString();

        return thisSong.compareTo(songToCompare);
    }

    private static String formatToComparableString(int i) {
        return String.format("%1$08d", i);
    }
}
