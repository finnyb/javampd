/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd;

import org.bff.javampd.integrationdata.DataLoader;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
