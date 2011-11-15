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

    private static MPDDatabase database;
    private static MPD mpd;
    private static MPDPlaylist playlist;
    private static MPDPlayer player;
    private static MPDAdmin admin;

    static {
        try {
            mpd = Controller.getInstance().getMpd();
            Controller.getInstance().loadSongs();
            database = getMpd().getMPDDatabase();
            playlist = getMpd().getMPDPlaylist();
            player = getMpd().getMPDPlayer();
            admin = getMpd().getMPDAdmin();
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
    public static MPDDatabase getDatabase() {
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
    public static MPDPlaylist getPlaylist() {
        return playlist;
    }

    /**
     * @return the player
     */
    public static MPDPlayer getPlayer() {
        return player;
    }

    public static MPDAdmin getAdmin() {
        return admin;
    }
}
