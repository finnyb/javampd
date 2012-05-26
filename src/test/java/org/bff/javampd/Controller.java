/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd;

import org.bff.javampd.capture.CaptureMPD;
import org.bff.javampd.data.*;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDTimeoutException;
import org.bff.javampd.mock.MockMPD;
import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDArtist;
import org.bff.javampd.objects.MPDSong;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * @author Bill
 */
public class Controller {

    private Collection<MPDSong> testSongs;
    private List<File> testRootFiles;
    public static final String PROP_SERVER = "server";
    public static final String PROP_PATH = "path.testmp3";
    private static final String PROP_SERVER_PATH = "path.server.mp3";
    public static final String EXTENSION = ".mp3";
    public static final String FILE_PROPS = System.getProperty("user.dir") + "/src/test/resources/TestProperties.properties";
    private static final String PROP_VERSION = "mpd.version";
    private static final String PROP_PASSWORD = "password";
    private String path;
    private String server;
    private int port;
    private MPD mpd;
    private static String version;
    private static final String URL_PREFIX = "";
    private static final String PROP_PORT = "port";
    private static final String PROP_MOCK = "mock";
    private static final String PROP_CAPTURE = "capture";
    private static Controller instance;
    private static final int INDEX_ARTIST = 0;
    private static final int INDEX_ALBUM = 1;
    private static final int INDEX_TRACK = 2;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_YEAR = 4;
    private static final int INDEX_GENRE = 5;
    private static final int INDEX_COMMENT = 6;
    private static final int INDEX_DISC = 7;
    private static final String NULL_TITLE = "";
    private static final String NULL_ALBUM = "";
    private static final String NULL_ARTIST = "";
    private static final String NULL_DISC = "";
    private static final String NULL_YEAR = "No Year";
    private static final String NULL_GENRE = "No Genre";
    private static final String NULL_COMMENT = null;
    private static final String NULL_TRACK = "0";
    private Collection<MPDArtist> artists;
    private Collection<MPDSong> songs;
    private Collection<MPDAlbum> albums;
    private List<MPDSong> databaseSongs;
    private Collection<String> genres;
    private Collection<String> years;
    private boolean songsLoaded;
    private String serverPath;
    private String password;
    private final MPDDatabase database;
    private boolean mock;
    private boolean capture;

    private Controller() throws IOException, MPDConnectionException {
        loadProperties();
        this.mpd = getNewMPD();
        this.database = mpd.getMPDDatabase();
    }

    /**
     * Returns a new mpd instance using the server, port, and password that has been set.
     *
     * @return a {@link MPD} instance
     */
    public MPD getNewMPD() throws MPDConnectionException, UnknownHostException {
        MPD newMPD;
        if (isMock()) {
            newMPD = new MockMPD(this.server, this.port, this.password);
        } else {
            if (isCapture()) {
                newMPD = createCaptureMpd(this.server, this.port, this.password);
            } else {
                newMPD = new MPD(this.server, this.port, this.password);
            }
        }

        return newMPD;
    }

    private MPD createCaptureMpd(String server, int port, String password) throws UnknownHostException, MPDConnectionException {
        MPD mpd;
        try {
            mpd = new CaptureMPD(server, port, password);
        } catch (UnknownHostException e) {
            CaptureMPD.writeToFile("Class: Controller");
            CaptureMPD.writeToFile("\tCommand: constructor");
            CaptureMPD.writeToFile("\t\tParam:" + server);
            CaptureMPD.writeToFile("\t\tParam:" + port);
            CaptureMPD.writeToFile("\t\tParam:" + password);
            CaptureMPD.writeToFile("\t\t\tUnknownHostException: " + e.getMessage());
            throw e;
        } catch (MPDConnectionException e) {
            CaptureMPD.writeToFile("Class: Controller");
            CaptureMPD.writeToFile("\tCommand: constructor");
            CaptureMPD.writeToFile("\t\tParam:" + server);
            CaptureMPD.writeToFile("\t\tParam:" + port);
            CaptureMPD.writeToFile("\t\tParam:" + password);
            CaptureMPD.writeToFile("\t\t\tConnectionException: " + e.getMessage());
            throw e;
        }

        return mpd;
    }

    /**
     * Returns a new mpd instance using the passed server, port, and password.
     *
     * @param server   the server to connect to
     * @param port     the port to connect on
     * @param password the password to use
     * @return a new instance of a MPD
     * @throws IOException
     * @throws MPDConnectionException
     */
    public MPD getNewMPD(String server, int port, String password) throws IOException, MPDConnectionException {
        MPD newMPD;
        if (isMock()) {
            newMPD = new MockMPD(server, port, password);
        } else {
            if (isCapture()) {
                newMPD = createCaptureMpd(server, port, password);
            } else {
                newMPD = new MPD(server, port, password);
            }
        }

        return newMPD;
    }

    public MPD getNewMPD(String server, int port, int timeout) throws IOException, MPDConnectionException {
        MPD newMPD;
        if (isMock()) {
            newMPD = new MockMPD(server, port, timeout);
        } else {
            if (isCapture()) {
                try {
                    newMPD = new CaptureMPD(server, port, timeout);
                } catch (UnknownHostException e) {
                    CaptureMPD.writeToFile("Class: Controller");
                    CaptureMPD.writeToFile("\tCommand: constructor");
                    CaptureMPD.writeToFile("\t\tParam:" + server);
                    CaptureMPD.writeToFile("\t\tParam:" + port);
                    CaptureMPD.writeToFile("\t\tParam:" + timeout);
                    CaptureMPD.writeToFile("\t\t\tUnknownHostException: " + e.getMessage());
                    throw e;
                } catch (MPDTimeoutException e) {
                    CaptureMPD.writeToFile("Class: Controller");
                    CaptureMPD.writeToFile("\tCommand: constructor");
                    CaptureMPD.writeToFile("\t\tParam:" + server);
                    CaptureMPD.writeToFile("\t\tParam:" + port);
                    CaptureMPD.writeToFile("\t\tParam:" + timeout);
                    CaptureMPD.writeToFile("\t\t\tConnectionException: " + e.getMessage());
                    throw e;
                }
            } else {
                newMPD = new MPD(server, port, timeout);
            }
        }

        return newMPD;
    }

    public static Controller getInstance() throws IOException, MPDConnectionException {
        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    private void loadProperties() throws IOException {
        Properties props = new Properties();

        InputStream in = null;
        try {
            in = new FileInputStream(FILE_PROPS);
            props.load(in);

            setServer(props.getProperty(PROP_SERVER));
            setPath(props.getProperty(PROP_PATH));
            setPort(Integer.parseInt(props.getProperty(PROP_PORT)));
            setServerPath(props.getProperty(PROP_SERVER_PATH));
            this.password = props.getProperty(PROP_PASSWORD);
            version = props.getProperty(PROP_VERSION);
            setMock(Boolean.parseBoolean(props.getProperty(PROP_MOCK, "true")));
            setCapture(Boolean.parseBoolean(props.getProperty(PROP_CAPTURE, "false")));
        } finally {
            assert in != null;
            in.close();
        }
    }

    public void loadSongs() throws MPDException {
        if (!songsLoaded) {
            songs = new ArrayList<MPDSong>();
            artists = new ArrayList<MPDArtist>();
            albums = new ArrayList<MPDAlbum>();
            genres = new ArrayList<String>();
            years = new ArrayList<String>();

            loadSongs(new File(getPath()));

            Songs.setTestSongs(new ArrayList<MPDSong>(getSongs()));
            Albums.setTestAlbums(new ArrayList<MPDAlbum>(getAlbums()));
            Artists.setTestArtists(new ArrayList<MPDArtist>(getArtists()));
            Genres.setTestGenres(new ArrayList<String>(getGenres()));
            Years.setTestYears(new ArrayList<String>(getYears()));
            setDatabaseSongs(new ArrayList<MPDSong>(Songs.databaseSongMap.values()));
            songsLoaded = true;
        }
    }

    /**
     * Artist - Album - Track - Title - Year - Genre - Comment - Disc Number
     *
     * @param f the file
     * @throws MPDException if there are issues :)
     */
    public void loadSongs(File f) throws MPDException {
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadSongs(file);
            } else {
                if (file.getName().endsWith(EXTENSION)) {
                    String[] s = file.getName().replace(EXTENSION, "").split("-");
                    MPDSong song = new MPDSong();
                    song.setFile(URL_PREFIX + (f.isDirectory() ? f.getName() : "") + "/" + file.getName());

                    MPDAlbum album = null;
                    MPDArtist artist = null;
                    String genre = null;
                    String year = "";

                    for (int j = 0; j < s.length; j++) {
                        switch (j) {
                            case INDEX_TITLE:
                                song.setTitle("null".equalsIgnoreCase(s[INDEX_TITLE]) ? NULL_TITLE : s[INDEX_TITLE]);
                                break;

                            case INDEX_ALBUM:
                                album = new MPDAlbum("null".equalsIgnoreCase(s[INDEX_ALBUM]) ? NULL_ALBUM : s[INDEX_ALBUM]);
                                break;

                            case INDEX_ARTIST:
                                artist = new MPDArtist("null".equalsIgnoreCase(s[INDEX_ARTIST]) ? NULL_ARTIST : s[INDEX_ARTIST]);
                                break;

                            case INDEX_COMMENT:
                                song.setComment("null".equalsIgnoreCase(s[INDEX_COMMENT]) ? NULL_COMMENT : s[INDEX_COMMENT]);
                                break;

                            case INDEX_GENRE:
                                genre = "null".equalsIgnoreCase(s[INDEX_GENRE]) ? NULL_GENRE : s[INDEX_GENRE];
                                break;

                            case INDEX_TRACK:
                                song.setTrack(Integer.parseInt("null".equalsIgnoreCase(s[INDEX_TRACK]) ? NULL_TRACK : s[INDEX_TRACK]));
                                break;

                            case INDEX_YEAR:
                                year = "null".equalsIgnoreCase(s[INDEX_YEAR]) ? NULL_YEAR : s[INDEX_YEAR];
                                break;

                            case INDEX_DISC:
                                song.setDiscNumber("null".equalsIgnoreCase(s[INDEX_DISC]) ? NULL_DISC : s[INDEX_DISC]);
                                break;
                        }
                    }

                    boolean found = false;
                    for (MPDArtist a : artists) {
                        if (a.getName().equals(artist.getName())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        artists.add(artist);
                    }

                    found = false;
                    for (MPDAlbum a : albums) {
                        if (a.getName().equals(album.getName())) {
                            found = true;
                        }
                    }

                    if (!found) {
                        albums.add(album);
                    }

                    for (MPDAlbum a : albums) {
                        if (a.getName().equals(album.getName())) {
                            album = a;
                        }
                    }
                    for (MPDArtist a : artists) {
                        if (a.getName().equals(album.getName())) {
                            artist = a;
                        }
                    }
                    song.setAlbum(album);
                    song.setArtist(artist);
                    song.setYear(year);
                    song.setGenre(genre);

                    fillSongId(song);

                    songs.add(song);

                    if (Songs.SONG_ARTIST_MAP.get(artist) == null) {
                        Songs.SONG_ARTIST_MAP.put(artist, new ArrayList<MPDSong>());
                    }

                    Songs.SONG_ARTIST_MAP.get(artist).add(song);

                    if (Artists.TEST_ARTIST_ALBUM_MAP.get(artist) == null) {
                        Artists.TEST_ARTIST_ALBUM_MAP.put(artist, new ArrayList<MPDAlbum>());
                    }

                    if (!Artists.TEST_ARTIST_ALBUM_MAP.get(artist).contains(album)) {
                        Artists.TEST_ARTIST_ALBUM_MAP.get(artist).add(album);
                    }

                    if (Songs.SONG_ARTIST_MAP.get(artist) == null) {
                        Songs.SONG_ARTIST_MAP.put(artist, new ArrayList<MPDSong>());
                    }

                    if (!Songs.SONG_ARTIST_MAP.get(artist).contains(song)) {
                        Songs.SONG_ARTIST_MAP.get(artist).add(song);
                    }

                    found = false;
                    for (String g : genres) {
                        if (g.equals(genre)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        genres.add(genre);
                    }

                    if (Genres.GENRE_ALBUM_MAP.get(genre) == null) {
                        Genres.GENRE_ALBUM_MAP.put(genre, new ArrayList<MPDAlbum>());
                    }

                    if (!Genres.GENRE_ALBUM_MAP.get(genre).contains(album)) {
                        Genres.GENRE_ALBUM_MAP.get(genre).add(album);
                    }

                    found = false;
                    for (String y : years) {
                        if (y.equals(year.equals(NULL_YEAR) ? "" : year)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        years.add(year.equals(NULL_YEAR) ? "" : year);
                    }

                    if (Years.YEAR_ALBUM_MAP.get(year) == null) {
                        Years.YEAR_ALBUM_MAP.put(year, new ArrayList<MPDAlbum>());
                    }

                    if (!Years.YEAR_ALBUM_MAP.get(year).contains(album)) {
                        Years.YEAR_ALBUM_MAP.get(year).add(album);
                    }
                }
            }
        }
    }

    public void fillSongId(MPDSong song) throws MPDException {
        MPDSong s = new ArrayList<MPDSong>(getDatabase().searchFileName(song.getFile())).get(0);
        try {
            Songs.databaseSongMap.put(s.getFile(), s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        song.setId(s.getId());
    }

    /**
     * @return the testSongs
     */
    public Collection<MPDSong> getTestSongs() {
        return testSongs;
    }

    /**
     * @param testSongs the testSongs to set
     */
    public void setTestSongs(Collection<MPDSong> testSongs) {
        this.testSongs = testSongs;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the mpd
     */
    public MPD getMpd() {
        return mpd;
    }

    /**
     * @param mpd the mpd to set
     */
    public void setMpd(MPD mpd) {
        this.mpd = mpd;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the testFiles
     */
    public Collection<File> getRootTestFiles() {
        if (testRootFiles == null) {
            testRootFiles = new ArrayList<File>();
            File[] files = new File(getPath()).listFiles();
            for (File file : files) {
                if (!file.getName().startsWith(".svn")
                        && !file.getName().startsWith("ReadMe")
                        && !file.getName().startsWith("TestWaveFile")) {
                    testRootFiles.add(file);
                }
            }
        }
        return testRootFiles;
    }

    public Collection<File> getTestFiles(File directory) {
        List<File> testFiles = new ArrayList<File>();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().startsWith(".svn")) {
                testFiles.add(file);
            }
        }

        return testFiles;
    }

    /**
     * @return the artists
     */
    public Collection<MPDArtist> getArtists() {
        return artists;
    }

    /**
     * @param artists the artists to set
     */
    public void setArtists(Collection<MPDArtist> artists) {
        this.artists = artists;
    }

    /**
     * @return the albums
     */
    public Collection<MPDAlbum> getAlbums() {
        return albums;
    }

    /**
     * @param albums the albums to set
     */
    public void setAlbums(Collection<MPDAlbum> albums) {
        this.albums = albums;
    }

    /**
     * @return the songs
     */
    public List<MPDSong> getDatabaseSongs() {
        return databaseSongs;
    }

    /**
     * @param songs the songs to set
     */
    public void setDatabaseSongs(List<MPDSong> songs) {
        this.databaseSongs = songs;
    }

    /**
     * @return the genres
     */
    public Collection<String> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(Collection<String> genres) {
        this.genres = genres;
    }

    /**
     * @return the years
     */
    public Collection<String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(Collection<String> years) {
        this.years = years;
    }

    /**
     * @return the serverPath
     */
    public String getServerPath() {
        return serverPath;
    }

    /**
     * @param serverPath the serverPath to set
     */
    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    /**
     * @return the songs
     */
    public Collection<MPDSong> getSongs() {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs(Collection<MPDSong> songs) {
        this.songs = songs;
    }

    /**
     * @return the database
     */
    public MPDDatabase getDatabase() {
        return database;
    }

    /**
     * @return the version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }
}
