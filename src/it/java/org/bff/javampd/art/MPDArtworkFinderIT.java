package org.bff.javampd.art;

import org.bff.javampd.BaseTest;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtist;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MPDArtworkFinderIT extends BaseTest {
    private ArtistDatabase artistDatabase;
    private ArtworkFinder artworkFinder;

    @BeforeEach
    public void before() {
        this.artistDatabase = getMpd().getMusicDatabase().getArtistDatabase();
        this.artworkFinder = getMpd().getArtworkFinder();
    }

    @Test
    public void findByArtist() throws Exception {
        String[] artistImages = new String[]{
                "artist200x200.png"
        };

        String[] albumImages = new String[]{
                "album200x200.png"
        };

        String prefix =
                decode(new File(this.getClass().getResource("/TestMp3s/id3v24/Artist2/" + artistImages[0])
                        .getFile())
                        .getParent()
                        .replaceAll("id3v24.*", ""));

        MPDArtist artist = this.artistDatabase.listArtistByName("Artist2");

        List<MPDArtwork> artworkList = this.artworkFinder.find(artist, prefix + File.separator);

        assertEquals(artistImages.length + albumImages.length, artworkList.size());

        String localArtistSuffix = "id3v24" + File.separator + "Artist2" + File.separator;
        String localAlbumSuffix = localArtistSuffix + "Album0" + File.separator;

        byte[] artistBytes = Files.readAllBytes(new File(prefix +
                localArtistSuffix + artistImages[0]).toPath());
        byte[] albumBytes = Files.readAllBytes(new File(prefix + localAlbumSuffix + albumImages[0]).toPath());

        Arrays.asList(artistImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertEquals(prefix + localArtistSuffix + image, foundArtwork.getPath());
            assertTrue(Arrays.equals(artistBytes, foundArtwork.getBytes()));
        });

        Arrays.asList(albumImages).forEach(image -> {
            MPDArtwork foundArtwork = artworkList
                    .stream()
                    .filter(artwork -> image.equals(artwork.getName()))
                    .findFirst()
                    .orElse(null);
            assertEquals(prefix + localAlbumSuffix + image, foundArtwork.getPath());
            assertTrue(Arrays.equals(albumBytes, foundArtwork.getBytes()));
        });
    }

    private String decode(String encodedString) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedString, "UTF-8");
    }
}