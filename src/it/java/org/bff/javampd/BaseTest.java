/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd;

import org.bff.javampd.exception.MPDException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bill
 */
public abstract class BaseTest {

    private static Database database;
    private static MPD mpd;
    private static Playlist playlist;
    private static Player player;
    private static Admin admin;
    private static StandAloneMonitor monitor;

    static {
        try {
            mpd = Controller.getInstance().getMpd();
            Controller.getInstance().loadSongs();
            database = getMpd().getDatabase();
            playlist = getMpd().getPlaylist();
            player = getMpd().getPlayer();
            admin = getMpd().getAdmin();
            monitor = getMpd().getMonitor();
        } catch (IOException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BaseTest() {
    }

    /**
     * @return the database
     */
    public static Database getDatabase() {
        return database;
    }

    /**
     * @return the mpd
     */
    public static MPD getMpd() {
        return mpd;
    }

    /**
     * @return the playlist
     */
    public static Playlist getPlaylist() {
        return playlist;
    }

    /**
     * @return the player
     */
    public static Player getPlayer() {
        return player;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static StandAloneMonitor getMonitor() {
        return monitor;
    }
}
