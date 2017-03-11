package org.bff.javampd.album;

import org.bff.javampd.MPDItem;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
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
     * Constructs an album wiht an artist name
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

    /**
     * We consider two albums to be equal if the {@link MPDAlbum}
     * names match and if the {@link org.bff.javampd.artist.MPDArtist}
     * name matches and the Date matches
     *
     * @param object the {@link MPDAlbum} to compare
     * @return true or false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        if (!super.equals(object)) {
            return false;
        }

        MPDAlbum album = (MPDAlbum) object;

        return this.getName().equals(album.getName()) &&
                compareArtists(album) &&
                compareDates(album);

    }

    private boolean compareDates(MPDAlbum album) {
        return getDate() != null && getDate().equals(album.getDate());
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    private boolean compareArtists(MPDAlbum album) {
        return getArtistName() != null && getArtistName().equals(album.getArtistName());
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
