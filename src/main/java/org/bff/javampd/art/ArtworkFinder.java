package org.bff.javampd.art;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;

import java.util.List;

/**
 * Artwork finder looks in the path of an {@link MPDArtist}, {@link MPDAlbum}
 * or path for images.
 * <p>
 * MPD must have at least read access to this directory or nothing will be returned.
 */
public interface ArtworkFinder {
    /**
     * Returns a list of {@link MPDArtwork} for images in the given {@link MPDAlbum}
     *
     * @param album the album path to interrogate
     * @return a list of {@link MPDArtwork}
     */
    List<MPDArtwork> find(MPDAlbum album);

    /**
     * Returns a list of {@link MPDArtwork} for images in the given {@link MPDAlbum}
     *
     * @param album      the album path to interrogate
     * @param pathPrefix the prefix of the path if not running locally with the MPD server
     * @return a list of {@link MPDArtwork}
     */
    List<MPDArtwork> find(MPDAlbum album, String pathPrefix);

    /**
     * Returns a list of {@link MPDArtwork} for images in the given {@link MPDArtist}
     * This will search both the artist path and all albums by the artist.  This assumes you have the artist as part
     * of the directory structure similar to this: <p/>'/music/artist/album/song.flac'.  If the artist is not there
     * no attempt will be made to search the artist directory.
     *
     * @param artist the artist path to interrogate
     * @return a list of {@link MPDArtwork}
     */
    List<MPDArtwork> find(MPDArtist artist);

    /**
     * Returns a list of {@link MPDArtwork} for images in the given {@link MPDArtist}
     * This will search both the artist path and all albums by the artist.  This assumes you have the artist as part
     * of the directory structure similar to this: <p/>'/music/artist/album/song.flac'.  If the artist is not there
     * no attempt will be made to search the artist directory.
     *
     * @param artist the artist path to interrogate
     *               * @param pathPrefix the prefix of the path if not running locally with the MPD server
     * @return a list of {@link MPDArtwork}
     */
    List<MPDArtwork> find(MPDArtist artist, String pathPrefix);

    /**
     * Returns a list of {@link MPDArtwork} for images in the given path
     *
     * @param path the path to interrogate
     * @return a list of {@link MPDArtwork}
     */
    List<MPDArtwork> find(String path);
}
