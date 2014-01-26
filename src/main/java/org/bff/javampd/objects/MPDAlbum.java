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
     * names match and if the {@link String} names match
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

        // Now we know the two albums have the same name but
        // if super.equals(object) returns true it could
        // still be the case that two albums have different artists.
        // So we have to check the artists:

        MPDAlbum albumToCompare = (MPDAlbum) object;
        // we know object
        // is of type MDPAlbum because otherwise super.equals(object)
        // would have returned false

        if (artistName == null || albumToCompare.artistName == null) {
            // we already know the album names are equal
            return true;
        } else {
            return artistName.equals(albumToCompare.artistName);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
