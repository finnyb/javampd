package org.bff.javampd.album;

import org.bff.javampd.MPDItem;

/**
 * MPDAlbum represents an album
 *
 * @author Bill
 */
public class MPDAlbum extends MPDItem {

    private String artistName;

    /**
     * Constructs an album
     *
     * @param name       name of the album
     * @param artistName artist of the album
     */
    public MPDAlbum(String name, String artistName) {
        super(name);
        this.artistName = artistName;
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
     * We consider two albums to be equal if the {@link MPDAlbum}
     * names match and if the {@link org.bff.javampd.artist.MPDArtist}
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

        MPDAlbum album = (MPDAlbum) object;

        return this.getName().equals(album.getName()) &&
                compareArtists(album);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    private boolean compareArtists(MPDAlbum album) {
        return artistName != null && artistName.equals(album.artistName);
    }
}
