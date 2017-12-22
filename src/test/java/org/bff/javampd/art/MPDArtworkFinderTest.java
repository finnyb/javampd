package org.bff.javampd.art;

import org.bff.javampd.MPDException;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDArtworkFinderTest {
    private ArtworkFinder artworkFinder;

    @Mock
    private SongDatabase songDatabase;

    @Before
    public void before() {
        artworkFinder = new MPDArtworkFinder(this.songDatabase);
    }

    @Test
    public void findArtist() throws Exception {
        String[] artistImages = new String[]{
                "artist200x200.jpg",
                "artist200x200.png"
        };

        String[] albumImages = new String[]{
                "album200x200.jpg",
                "album200x200.png"
        };

        MPDArtist artist = new MPDArtist("artist");

        String prefix = decode(new File(this.getClass().getResource("/images/artist/" + artistImages[0]).getFile()).getParent())
                + File.separator;

        String albumSuffix = "album" + File.separator;

        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong(prefix + albumSuffix + "song1", "song1"));
        songs.add(new MPDSong(prefix + albumSuffix + "song2", "song2"));

        when(songDatabase.findArtist(artist)).thenReturn(songs);

        List<MPDArtwork> artworkList = artworkFinder.find(artist);

        assertEquals(artistImages.length + albumImages.length, artworkList.size());

        Map<String, byte[]> btyeMap = new HashMap<>();
        btyeMap.put(artistImages[0], Files.readAllBytes(new File(prefix + artistImages[0]).toPath()));
        btyeMap.put(artistImages[1], Files.readAllBytes(new File(prefix + artistImages[1]).toPath()));
        btyeMap.put(albumImages[0], Files.readAllBytes(new File(prefix + albumSuffix + albumImages[0]).toPath()));
        btyeMap.put(albumImages[1], Files.readAllBytes(new File(prefix + albumSuffix + albumImages[1]).toPath()));


        Arrays.asList(artistImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertEquals(prefix + image, foundArtwork.getPath());
            assertTrue(Arrays.equals(btyeMap.get(foundArtwork.getName()), foundArtwork.getBytes()));
        });

        Arrays.asList(albumImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertEquals(prefix + albumSuffix + image, foundArtwork.getPath());
            assertTrue(Arrays.equals(btyeMap.get(foundArtwork.getName()), foundArtwork.getBytes()));
        });
    }

    @Test
    public void findArtistPrefix() throws Exception {
        String[] artistImages = new String[]{
                "artist200x200.jpg",
                "artist200x200.png"
        };

        String[] albumImages = new String[]{
                "album200x200.jpg",
                "album200x200.png"
        };

        MPDArtist artist = new MPDArtist("artist");

        String path = decode(new File(this.getClass().getResource("/images/artist/" + artistImages[0]).getFile()).getParent());
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("/album/song1", "song1"));
        songs.add(new MPDSong("/album/song2", "song2"));

        when(songDatabase.findArtist(artist)).thenReturn(songs);

        List<MPDArtwork> artworkList = artworkFinder.find(artist, path);

        assertEquals(artistImages.length + albumImages.length, artworkList.size());

        Arrays.asList(artistImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(foundArtwork.getPath());
            assertNotNull(foundArtwork.getBytes());
        });

        Arrays.asList(albumImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(foundArtwork.getPath());
            assertNotNull(foundArtwork.getBytes());
        });
    }

    @Test
    public void findArtistBadPath() throws Exception {
        String[] artistImages = new String[]{
                "artist200x200.png"
        };

        MPDArtist artist = new MPDArtist("artist");

        String path1 = decode(new File(this.getClass().getResource("/images/" + artistImages[0]).getFile()).getParent());
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong(path1, "song1"));

        when(songDatabase.findArtist(artist)).thenReturn(songs);

        List<MPDArtwork> artworkList = artworkFinder.find(artist);

        assertEquals(0, artworkList.size());
    }

    @Test
    public void findAlbum() throws Exception {
        String[] albumImages = new String[]{
                "album200x200.jpg",
                "album200x200.png"
        };

        MPDAlbum album = new MPDAlbum("album", "artist");

        String path1 = decode(new File(this.getClass().getResource("/images/artist/album/" + albumImages[0]).getFile()).getParent());
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong(path1 + "/song1", "song1"));

        when(songDatabase.findAlbum(album)).thenReturn(songs);

        List<MPDArtwork> artworkList = artworkFinder.find(album);

        assertEquals(albumImages.length, artworkList.size());

        Arrays.asList(albumImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(foundArtwork.getPath());
            assertNotNull(foundArtwork.getBytes());
        });
    }

    @Test
    public void findAlbumPrefix() throws Exception {
        String[] albumImages = new String[]{
                "album200x200.jpg",
                "album200x200.png"
        };

        MPDAlbum album = new MPDAlbum("album", "artist");

        String path = decode(new File(this.getClass().getResource("/images/artist/album/" + albumImages[0]).getFile()).getParent());
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("/song1", "song1"));

        when(songDatabase.findAlbum(album)).thenReturn(songs);

        List<MPDArtwork> artworkList = artworkFinder.find(album, path);

        assertEquals(albumImages.length, artworkList.size());

        Arrays.asList(albumImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(foundArtwork.getPath());
            assertNotNull(foundArtwork.getBytes());
        });
    }

    @Test
    public void findPath() throws Exception {
        String[] images = new String[]{
                "artist200x200.jpg",
                "artist200x200.png"
        };

        String testImage = decode(new File(this.getClass().getResource("/images/artist/" + images[0]).getFile()).getParent());
        List<MPDArtwork> artworkList = artworkFinder.find(testImage);

        assertEquals(images.length, artworkList.size());

        Arrays.asList(images).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(foundArtwork.getPath());
            assertNotNull(foundArtwork.getBytes());
        });
    }

    @Test(expected = MPDException.class)
    public void findPathIOException() throws Exception {
        File testFile = File.createTempFile("test", ".jpg");

        testFile.setReadable(false);

        artworkFinder.find(testFile.getParent());
    }

    @Test(expected = MPDException.class)
    public void findPathDirectoryIOException() throws Exception {
        String javaTempDir = System.getProperty("java.io.tmpdir");
        File tempDir = new File(javaTempDir + (javaTempDir.endsWith(File.separator) ? "" : File.separator) + "imageTemp");
        tempDir.mkdir();
        tempDir.setWritable(true);

        File testFile = null;
        try {
            testFile = File.createTempFile("test", ".jpg", tempDir);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        tempDir.setReadable(false);
        artworkFinder.find(testFile.getParent());

    }

    @Test(expected = MPDException.class)
    public void findBadPath() throws Exception {
        List<MPDArtwork> artworkList = artworkFinder.find("bad");

        assertEquals(2, artworkList.size());
    }

    private String decode(String encodedString) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedString, "UTF-8");
    }
}