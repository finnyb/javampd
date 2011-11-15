/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.objects;

/**
 * MPDAlbum represents an album
 *
 * @author Bill Findeisen
 */
public class MPDAlbum extends MPDItem {

    private MPDArtist artist;

    /**
     * Creates a album object
     *
     * @param name the name of the album
     */
    public MPDAlbum(String name) {
        setName(name);
    }

    /**
     * Returns the {@link MPDArtist} for the album.
     *
     * @return the {@link MPDArtist} for the album
     */
    public MPDArtist getArtist() {
        return artist;
    }

    /**
     * Sets the {@link MPDArtist} for the album
     *
     * @param artist the {@link MPDArtist} for the album
     */
    public void setArtist(MPDArtist artist) {
        this.artist = artist;
    }

    /**
     * We consider two albums to be equal if the {@link MPDAlbum}
     * names match and if the {@link MPDArtist} names match
     *
     * @param object the {@link MPDAlbum} to compare
     * @return true or false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
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

        if (artist == null || albumToCompare.artist == null) {
            // we already know the album names are equal
            return true;
        } else {
            try {
                return artist.equals(albumToCompare.artist);
            } catch (NullPointerException nex) {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        try {
            return super.toString() + " - " + artist.getName();
        } catch (NullPointerException nex) {
            return super.toString();
        }
    }
}
