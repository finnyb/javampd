/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.integrationdata.DataLoader;
import org.bff.javampd.integrationdata.Songs;
import org.bff.javampd.objects.MPDSong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private static ServerStatistics serverStatistics;

    static {
        try {
            mpd = new MPD(TestProperties.getInstance().getServer(),
                    TestProperties.getInstance().getPort(),
                    TestProperties.getInstance().getPassword());
            database = getMpd().getDatabase();
            playlist = getMpd().getPlaylist();
            player = getMpd().getPlayer();
            admin = getMpd().getAdmin();
            monitor = getMpd().getMonitor();
            serverStatistics = getMpd().getServerStatistics();
            DataLoader.loadData(new File(TestProperties.getInstance().getPath()));
            for (MPDSong song : Songs.songs) {
                fillSongId(song);
            }
        } catch (IOException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MPDException ex) {
            Logger.getLogger(BaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BaseTest() {
    }

    public static void fillSongId(MPDSong song) throws MPDException {
        MPDSong s = new ArrayList<>(getDatabase().searchFileName(song.getFile())).get(0);
        try {
            Songs.databaseSongs.add(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        song.setId(s.getId());
    }

    public MPD getNewMpd() throws IOException, MPDConnectionException {
        return new MPD(TestProperties.getInstance().getServer(),
                TestProperties.getInstance().getPort(),
                TestProperties.getInstance().getPassword());
    }

    public MPD getNewMpd(String password) throws IOException, MPDConnectionException {
        return new MPD(TestProperties.getInstance().getServer(),
                TestProperties.getInstance().getPort(),
                password);
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

    public static ServerStatistics getServerStatistics() {
        return serverStatistics;
    }
}
