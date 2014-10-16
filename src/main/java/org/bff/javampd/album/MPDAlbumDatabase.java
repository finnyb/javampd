package org.bff.javampd.album;

import com.google.inject.Inject;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the {@link org.bff.javampd.server.MPD#getArtistDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDAlbumDatabase implements AlbumDatabase {

    private TagLister tagLister;

    @Inject
    public MPDAlbumDatabase(TagLister tagLister) {
        this.tagLister = tagLister;
    }

    @Override
    public Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDDatabaseException {
        List<String> list = new ArrayList<>();
        list.add(artist.getName());

        List<MPDAlbum> albums = new ArrayList<>();
        for (String albumName : tagLister.list(TagLister.ListType.ALBUM, list)) {
            MPDAlbum album = new MPDAlbum(albumName, artist.getName());
            albums.add(album);
        }
        return albums;
    }

    @Override
    public Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDDatabaseException {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDAlbum> albums = new ArrayList<>();
        for (String albumName : tagLister.list(TagLister.ListType.ALBUM, list)) {
            albums.addAll(lookupAlbumByName(albumName, list));
        }
        return albums;
    }

    @Override
    public Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDDatabaseException {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.DATE.getType());
        list.add(year);

        List<MPDAlbum> albums = new ArrayList<>();
        for (String albumName : tagLister.list(TagLister.ListType.ALBUM, list)) {
            albums.addAll(lookupAlbumByName(albumName, list));
        }
        return albums;
    }

    private Collection<MPDAlbum> lookupAlbumByName(String albumName,
                                                   List<String> params) throws MPDDatabaseException {
        List<MPDAlbum> albums = new ArrayList<>();
        List<String> tagParams = new ArrayList<>();
        tagParams.add(TagLister.ListType.ALBUM.getType());
        tagParams.add(albumName);

        Collection<String> allArtists = tagLister.list(TagLister.ListType.ARTIST, tagParams);
        Collection<String> artists = tagLister.list(TagLister.ListType.ARTIST, params);
        allArtists.retainAll(artists);

        for (String artist : allArtists) {
            MPDAlbum album = new MPDAlbum(albumName, artist);
            albums.add(album);
        }

        return albums;
    }
}
