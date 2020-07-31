package org.bff.javampd;

import org.awaitility.Awaitility;
import org.bff.javampd.integrationdata.DataLoader;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Bill
 */
public abstract class BaseTest {

    private static MPD mpd;

    static {
        try {
            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .password(TestProperties.getInstance().getPassword())
                    .build();

            DataLoader.loadData(new File(TestProperties.getInstance().getPath()));
            TestSongs.getSongs().forEach(BaseTest::loadMPDSong);
            Awaitility.setDefaultTimeout(5, TimeUnit.MINUTES);
        } catch (IOException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loadMPDSong(MPDSong song) {
        MPDSong s = new ArrayList<>(getMpd().getMusicDatabase().getSongDatabase().searchFileName(song.getFile())).get(0);
        try {
            MPDSongs.getSongs().add(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        song.setId(s.getId());
    }

    public MPD getNewMpd() throws IOException, MPDConnectionException {
        return new MPD.Builder()
                .server(TestProperties.getInstance().getServer())
                .port(TestProperties.getInstance().getPort())
                .password(TestProperties.getInstance().getPassword())
                .build();
    }

    public MPD getNewMpd(String password) throws IOException, MPDConnectionException {
        return new MPD.Builder()
                .server(TestProperties.getInstance().getServer())
                .port(TestProperties.getInstance().getPort())
                .password(password)
                .build();
    }

    /**
     * @return the mpd
     */
    public static MPD getMpd() {
        return mpd;
    }

    public void compareSongLists(List<MPDSong> testResults, List<MPDSong> foundSongs) {

        if (testResults.isEmpty()) {
            assertTrue("Bad test criteria.  Should have a size of at least 1", false);
        }

        assertEquals(testResults.size(), foundSongs.size());

        for (MPDSong song : testResults) {
            boolean found = false;
            for (MPDSong songDb : foundSongs) {
                if (song.getFile().equals(songDb.getFile())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Logger.getLogger(BaseTest.class.getName()).log(Level.WARNING, "Unable to find song " + song.getFile());
            }
            assertTrue(found);
        }
    }
}
