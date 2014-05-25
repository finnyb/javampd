/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mockeddata;

import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDArtist;
import org.bff.javampd.objects.MPDGenre;
import org.bff.javampd.objects.MPDSong;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Song ids must be filled since not reliable
 *
 * @author bill
 */
public class Songs {
    public static final String EXTENSION = ".mp3";

    private static final int INDEX_ARTIST = 0;
    private static final int INDEX_ALBUM = 1;
    private static final int INDEX_TRACK = 2;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_YEAR = 4;
    private static final int INDEX_GENRE = 5;
    private static final int INDEX_COMMENT = 6;
    private static final int INDEX_DISC = 7;
    private static final String NULL_TITLE = null;
    @SuppressWarnings("unused")
	private static final String NULL_ALBUM = "";
    @SuppressWarnings("unused")
	private static final String NULL_ARTIST = "";
    private static final String NULL_DISC = "";
    private static final String NULL_COMMENT = null;
    private static final String NULL_TRACK = "0";

    public static Collection<MPDSong> songs = new ArrayList<>();

    public static final HashMap<MPDArtist, Collection<MPDSong>> SONG_ARTIST_MAP =
            new HashMap<MPDArtist, Collection<MPDSong>>();

    public static void loadSong(File file, File f) {
        String[] s = file.getName().replace(EXTENSION, "").split("-");
        MPDSong song = new MPDSong();
        song.setFile(file.getName());
        song.setFile((f.isDirectory() ? f.getName() : "") + "/" + file.getName());

        MPDAlbum album = null;
        MPDArtist artist = null;
        MPDGenre genre = null;
        String year = "";

        for (int j = 0; j < s.length; j++) {
            switch (j) {
                case INDEX_TITLE:
                    song.setTitle("null".equalsIgnoreCase(s[INDEX_TITLE]) ? NULL_TITLE : s[INDEX_TITLE]);
                    break;

                case INDEX_ALBUM:
                    album = processAlbum(song, s[INDEX_ALBUM]);
                    break;

                case INDEX_ARTIST:
                    artist = processArtist(song, s[INDEX_ARTIST]);
                    break;

                case INDEX_COMMENT:
                    song.setComment("null".equalsIgnoreCase(s[INDEX_COMMENT]) ? NULL_COMMENT : s[INDEX_COMMENT]);
                    break;

                case INDEX_GENRE:
                    genre = processGenre(song, s[INDEX_GENRE]);
                    break;

                case INDEX_TRACK:
                    song.setTrack(Integer.parseInt("null".equalsIgnoreCase(s[INDEX_TRACK]) ? NULL_TRACK : s[INDEX_TRACK]));
                    break;

                case INDEX_YEAR:
                    year = "null".equalsIgnoreCase(s[INDEX_YEAR]) ? Years.NULL_YEAR : s[INDEX_YEAR];
                    break;

                case INDEX_DISC:
                    song.setDiscNumber("null".equalsIgnoreCase(s[INDEX_DISC]) ? NULL_DISC : s[INDEX_DISC]);
                    break;
            }
        }

        song.setYear(year);
        Years.addYear(year);
        Years.addYear(year, album);

        Artists.addAlbum(artist, album);
        Genres.addGenre(genre, album);

        songs.add(song);

        if (SONG_ARTIST_MAP.get(artist) == null) {
            SONG_ARTIST_MAP.put(artist, new ArrayList<MPDSong>());
        }

        if (!SONG_ARTIST_MAP.get(artist).contains(song)) {
            SONG_ARTIST_MAP.get(artist).add(song);
        }
    }

    private static MPDAlbum processAlbum(MPDSong song, String albumName) {
        if (!"null".equalsIgnoreCase(albumName)) {
            albumName = albumName.replace("[colon]", ":");
            song.setAlbumName(albumName != null ? albumName : null);
            return Albums.addAlbum(albumName);
        }

        return null;
    }

    private static MPDArtist processArtist(MPDSong song, String artistName) {
        if (!"null".equalsIgnoreCase(artistName)) {
            song.setArtistName(artistName != null ? artistName : null);
            return Artists.addArtist(artistName);
        }

        return null;
    }

    private static MPDGenre processGenre(MPDSong song, String genreName) {
        song.setGenre("null".equalsIgnoreCase(genreName) ? Genres.NULL_GENRE : genreName);
        return Genres.addGenre(song.getGenre());
    }
}