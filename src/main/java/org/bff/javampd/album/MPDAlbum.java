package org.bff.javampd.album;

import lombok.EqualsAndHashCode;
import org.bff.javampd.MPDItem;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
@EqualsAndHashCode(callSuper = true)
public class MPDAlbum extends MPDItem {

    private String artistName;
    private String date;
    private String genre;

    /**
     * Constructs an album
     *
     * @param name name of the album
     */
    public MPDAlbum(String name) {
        super(name);
        this.artistName = "";
        this.date = "";
        this.genre = "";
    }

    /**
     * Constructs an album with an artist name
     *
     * @param name       name of the album
     * @param artistName artist of the album
     */
    public MPDAlbum(String name, String artistName) {
        super(name);
        this.artistName = artistName;
        this.date = "";
        this.genre = "";
    }

    /**
     * Returns the {@link String} for the album.
     *
     * @return the {@link String} for the album
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets the artist name for this album
     *
     * @param artistName the artist's name
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    /**
     * the date of the album
     *
     * @return the date of the album
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the album
     *
     * @param date the date of the album
     */
    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
