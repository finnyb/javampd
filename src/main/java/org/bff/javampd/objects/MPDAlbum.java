/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.objects;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
public class MPDAlbum extends MPDItem {

    private String artistName;

    /**
     * Creates a album object
     *
     * @param name the name of the album
     */
    public MPDAlbum(String name) {
        setName(name);
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
     * Sets the {@link String} for the album
     *
     * @param artistName the {@link String} for the album
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * We consider two albums to be equal if the {@link MPDAlbum}
     * names match and if the {@link org.bff.javampd.objects.MPDArtist}
     * name matches
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

        return compareArtists((MPDAlbum) object);

    }

    private boolean compareArtists(MPDAlbum album) {
        if (artistName == null || album.artistName == null) {
            return true;
        } else {
            return artistName.equals(album.artistName);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
