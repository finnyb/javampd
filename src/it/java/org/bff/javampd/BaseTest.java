package org.bff.javampd;

import org.bff.javampd.integrationdata.DataLoader;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            for (MPDSong song : TestSongs.getSongs()) {
                loadMPDSong(song);
            }
        } catch (IOException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loadMPDSong(MPDSong song) throws MPDException {
        MPDSong s = new ArrayList<>(getMpd().getDatabaseManager().getSongDatabase().searchFileName(song.getFile())).get(0);
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

            assertTrue(found);
        }
    }
}
