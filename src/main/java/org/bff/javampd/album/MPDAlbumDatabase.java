package org.bff.javampd.album;

import com.google.inject.Inject;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.DatabaseManager#getArtistDatabase} method from
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
    public Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) {
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
    public Collection<String> listAllAlbumNames() {
        return tagLister.list(TagLister.ListType.ALBUM)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MPDAlbum> listAllAlbums() {
        List<MPDAlbum> albums = new ArrayList<>();

        for (String albumName : listAllAlbumNames()) {
            albums.addAll(findAlbum(albumName));
        }

        return albums;
    }

    @Override
    public Collection<MPDAlbum> listAllAlbums(int start, int end) {
        List<MPDAlbum> albums = new ArrayList<>();

        List<String> albumNames = new ArrayList<>(listAllAlbumNames());

        int endIndex = end <= albumNames.size() ? end : albumNames.size() - 1;

        if (start > endIndex) {
            throw new IllegalArgumentException("Start index must be smaller than end index");
        }

        for (String albumName : albumNames.subList(start, endIndex)) {
            albums.addAll(findAlbum(albumName));
        }

        return Collections.unmodifiableCollection(albums);
    }

    @Override
    public Collection<MPDAlbum> findAlbum(String albumName) {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.ALBUM.getType());
        list.add(albumName);

        return lookupAlbumByName(albumName, list);
    }

    @Override
    public MPDAlbum findAlbumByArtist(MPDArtist artist, String albumName) {
        for (MPDAlbum album : listAlbumsByArtist(artist)) {
            if (albumName.equalsIgnoreCase(album.getName())) {
                return album;
            }
        }

        return null;
    }

    @Override
    public Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) {
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
    public Collection<MPDAlbum> listAlbumsByYear(String year) {
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
                                                   List<String> artistParams) {
        List<MPDAlbum> albums = new ArrayList<>();
        List<String> tagParams = new ArrayList<>();
        tagParams.add(TagLister.ListType.ALBUM.getType());
        tagParams.add(albumName);

        Collection<String> allArtists = tagLister.list(TagLister.ListType.ARTIST, tagParams);
        Collection<String> artists = tagLister.list(TagLister.ListType.ARTIST, artistParams);
        allArtists.retainAll(artists);

        for (String artist : allArtists) {
            MPDAlbum album = new MPDAlbum(albumName, artist);
            albums.add(album);
        }

        return albums;
    }
}
