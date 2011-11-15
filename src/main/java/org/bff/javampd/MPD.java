/*
 * MPD.java
 *
 * Created on September 27, 2005, 1:34 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.exception.MPDTimeoutException;
import org.bff.javampd.monitor.MPDEventRelayer;
import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDArtist;
import org.bff.javampd.objects.MPDSong;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPD {

    private int port;
    private InetAddress serverAddress;
    private Socket socket;
    private String lastError;
    private Properties prop;
    private MPDPlayer mpdPlayer;
    private MPDPlaylist mpdPlaylist;
    private MPDDatabase mpdDatabase;
    private MPDEventRelayer mpdEventRelayer;
    private MPDAdmin mpdAdmin;
    private final String version;
    private static final int TRIES = 3;
    /**
     * The location of the mpd properties file.
     */
    private static final String PROPFILE = "/org/bff/javampd/mpd.properties";
    //property file keys
    private static final String MPDPROPSERVERENCODING = "MPD_SERVER_ENCODING";
    private static final String MPDPROPCLEARERROR = "MPD_CMD_CLEAR_ERROR";
    private static final String MPDPROPCLOSE = "MPD_CMD_CLOSE";
    private static final String MPDPROPKILL = "MPD_CMD_KILL";
    private static final String MPDPROPSTATUS = "MPD_CMD_STATUS";
    private static final String MPDPROPSTATS = "MPD_CMD_STATISTICS";
    private static final String MPDPROPSTARTBULK = "MPD_CMD_START_BULK";
    private static final String MPDPROPENDBULK = "MPD_CMD_END_BULK";
    private static final String MPDPROPPASSWORD = "MPD_CMD_PASSWORD";
    private static final String MPDPROPPING = "MPD_CMD_PING";
    private static final String MPDPROPRESPONSEERR = "MPD_CMD_RESPONSE_ERR";
    private static final String MPDPROPRESPONSEOK = "MPD_CMD_RESPONSE_OK";
    /**
     * the MPD prefix for the filename
     */
    protected static final String SONGPREFIXFILE = "file:";
    /**
     * the MPD prefix for the artist
     */
    protected static final String SONGPREFIXARTIST = "Artist:";
    /**
     * the MPD prefix for the album
     */
    protected static final String SONGPREFIXALBUM = "Album:";
    /**
     * the MPD prefix for the track number
     */
    protected static final String SONGPREFIXTRACK = "Track:";
    /**
     * the MPD prefix for the title
     */
    protected static final String SONGPREFIXTITLE = "Title:";
    /**
     * the MPD prefix for the date
     */
    protected static final String SONGPREFIXDATE = "Date:";
    /**
     * the MPD prefix for the genre
     */
    protected static final String SONGPREFIXGENRE = "Genre:";
    /**
     * the MPD prefix for the comment
     */
    protected static final String SONGPREFIXCOMMENT = "Comment:";
    /**
     * the MPD prefix for the time
     */
    protected static final String SONGPREFIXTIME = "Time:";
    /**
     * the MPD prefix for the playlist position
     */
    protected static final String SONGPREFIXPOS = "Pos:";
    /**
     * the MPD prefix for the song id
     */
    protected static final String SONGPREFIXID = "Id:";
    /**
     * the MPD prefix for disc
     */
    protected static final String SONGPREFIXDISC = "Disc:";
    /**
     * status of playing
     */
    protected static final String STATUS_PLAYING = "play";
    /**
     * status of paused
     */
    protected static final String STATUS_PAUSED = "Id:";
    /**
     * status of stopped
     */
    protected static final String STATUS_STOPPED = "Id:";
    /**
     * Default MPD port
     */
    private static final int MPD_DEFAULT_PORT = 6600;

    private enum Response {

        OK(MPDPROPRESPONSEOK),
        ERR(MPDPROPRESPONSEERR);
        private final String prop;

        Response(String prop) {
            this.prop = prop;
        }

        public String getProp() {
            return (prop);
        }
    }

    /**
     * Enumeration of the available information from MPD server
     * statistics.
     */
    protected enum StatList {

        /**
         * The total number of albums
         */
        ALBUMS("albums:"),
        /**
         * The total number of artists
         */
        ARTISTS("artists:"),
        /**
         * The total number of songs
         */
        SONGS("songs:"),
        /**
         * The sum of all song times in the database
         */
        DBPLAYTIME("db_playtime:"),
        /**
         * The last time the database was updated in UNIX time
         */
        DBUPDATE("db_update:"),
        /**
         * The time length of music played
         */
        PLAYTIME("playtime:"),
        /**
         * Daemon uptime in seconds
         */
        UPTIME("uptime:");
        private String prefix;

        /**
         * Default constructor for Statistics
         *
         * @param prefix the prefix of the line in the response
         */
        StatList(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Returns the <CODE>String</CODE> prefix of the response.
         *
         * @return the prefix of the response
         */
        public String getStatPrefix() {
            return (prefix);
        }
    }

    /**
     * Enumeration of the available information from the MPD
     * server status.
     */
    public enum StatusList {

        /**
         * The current volume (0-100)
         */
        VOLUME("volume:"),
        /**
         * is the song repeating (0 or 1)
         */
        REPEAT("repeat:"),
        /**
         * is the song playing in random order (0 or 1)
         */
        RANDOM("random:"),
        /**
         * the playlist version number (31-bit unsigned integer)
         */
        PLAYLIST("playlist:"),
        /**
         * the length of the playlist
         */
        PLAYLISTLENGTH("playlistlength:"),
        /**
         * the current state (play, stop, or pause)
         */
        STATE("state:"),
        /**
         * playlist song number of the current song stopped on or playing
         */
        CURRENTSONG("song:"),
        /**
         * playlist song id of the current song stopped on or playing
         */
        CURRENTSONGID("songid:"),
        /**
         * the time of the current playing/paused song
         */
        TIME("time:"),
        /**
         * instantaneous bitrate in kbps
         */
        BITRATE("bitrate:"),
        /**
         * crossfade in seconds
         */
        XFADE("xfade:"),
        /**
         * the current sample rate, bits, and channels
         */
        AUDIO("audio:"),
        /**
         * job id
         */
        UPDATINGSDB("updatings_db:"),
        /**
         * if there is an error, returns message here
         */
        ERROR("error:");
        private String prefix;

        /**
         * Enum constructor
         *
         * @param prefix the prefix of the line in the response
         */
        StatusList(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Returns the <CODE>String</CODE> prefix of the response.
         *
         * @return the prefix of the response
         */
        public String getStatusPrefix() {
            return (prefix);
        }
    }

    /**
     * Creates a new instance of MPD without authentication using the
     * default MPD port of 6600
     *
     * @param server the MPD Server
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                                       if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server) throws UnknownHostException, MPDConnectionException {
        this(server, MPD_DEFAULT_PORT);
    }

    /**
     * Creates a new instance of MPD without authentication
     *
     * @param server the MPD Server
     * @param port   the port MPD is listening on
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                                       if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port) throws UnknownHostException, MPDConnectionException {
        this(server, port, null);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server   the MPD server
     * @param port     the port MPD is listening on
     * @param password the password to authenticate with
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                                       if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, String password) throws UnknownHostException, MPDConnectionException {
        this(server, port, password, 0);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server  the MPD server
     * @param port    the port MPD is listening on
     * @param timeout the amount of time in milliseconds to wait for the MPD connection
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                                       if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, int timeout) throws UnknownHostException, MPDConnectionException {
        this(server, port, null, timeout);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server   the MPD server
     * @param port     the port MPD is listening on
     * @param password the password to authenticate with
     * @param timeout  the amount of time in milliseconds to wait for the MPD connection
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                                       if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, String password, int timeout) throws UnknownHostException, MPDConnectionException {
        try {
            loadMpdPropValues();
            this.serverAddress = InetAddress.getByName(server);
            this.port = port;
            this.version = connect(timeout);

            if (password != null) {
                authenticate(password);
            }
        } catch (UnknownHostException ex) {
            throw ex;
        } catch (MPDTimeoutException mte) {
            throw mte;
        } catch (Exception e) {
            throw new MPDConnectionException(e.getMessage());
        }
    }

    /**
     * Clears the current error message in the MPD status
     *
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public void clearerror() throws MPDConnectionException, MPDResponseException {
        sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPCLEARERROR)));
    }

    /**
     * Closes the connection to the MPD server.
     *
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public void close() throws MPDConnectionException, MPDResponseException {
        sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPCLOSE)));
    }

    /**
     * Returns the time length of the music played since the server was started.
     *
     * @return the time length of the music played
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public long getPlaytime() throws MPDConnectionException, MPDResponseException {
        return (Long.parseLong(getServerStat(StatList.PLAYTIME)));
    }

    /**
     * Returns the MPD server daemon uptime in seconds.
     *
     * @return the server uptime in seconds
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public long getUptime() throws MPDConnectionException, MPDResponseException {
        return (Long.parseLong(getServerStat(StatList.UPTIME)));
    }

    /**
     * Returns the MPD version running on the server.
     *
     * @return the version of the MPD
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns a {@link MPDPlayer} using this class as the connection
     * for the player.  Multiple calls to this method will return
     * the same {@link MPDPlayer} class.
     *
     * @return the mpd player
     */
    public synchronized MPDPlayer getMPDPlayer() {
        if (mpdPlayer == null) {
            mpdPlayer = new MPDPlayer(this);
        }
        return (mpdPlayer);
    }

    /**
     * Returns a {@link MPDPlaylist} using this class as the connection
     * for the playlist.  Multiple calls to this method will return
     * the same {@link MPDPlaylist} class.
     *
     * @return the mpd player
     */
    public synchronized MPDPlaylist getMPDPlaylist() {
        if (mpdPlaylist == null) {
            mpdPlaylist = new MPDPlaylist(this);
        }
        return (mpdPlaylist);
    }

    /**
     * Returns a {@link MPDDatabase} using this class as the connection
     * for the database.  Multiple calls to this method will return
     * the same {@link MPDDatabase} class.
     *
     * @return the mpd player
     */
    public synchronized MPDDatabase getMPDDatabase() {
        if (mpdDatabase == null) {
            mpdDatabase = new MPDDatabase(this);
        }
        return (mpdDatabase);
    }

    /**
     * Returns a {@link MPDAdmin} using this class as the connection
     * for the administrator.  Multiple calls to this method will return
     * the same {@link MPDAdmin} class.
     *
     * @return the mpd player
     */
    public synchronized MPDAdmin getMPDAdmin() {
        if (mpdAdmin == null) {
            mpdAdmin = new MPDAdmin(this);
        }
        return (mpdAdmin);
    }

    /**
     * Returns a {@link MPDEventRelayer} using this class as the connection.
     * Multiple calls to this method will return
     * the same {@link MPDEventRelayer} class.
     *
     * @return the MPDEventRelayer
     */
    public synchronized MPDEventRelayer getMPDEventRelayer() {
        if (mpdEventRelayer == null) {
            mpdEventRelayer = new MPDEventRelayer(this);
        }
        return (mpdEventRelayer);
    }

    /**
     * Determines if there is a connection to the MPD server.
     *
     * @return true if connected to server , false if not
     */
    public boolean isConnected() {
        if (ping()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.  To query specific statuses use {@link #getStatus(StatusList status)}.
     *
     * @return the desired status information
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public Collection<String> getStatus() throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPSTATUS));
        List<String> respList = new ArrayList<String>(sendMPDCommand(command));
        return (respList);
    }

    /**
     * Sends a {@link MPDCommand} and its parameters to the MPD server returning the
     * response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>.  Note to
     * check the response you may call {@link MPD#isResponseOK(String)} )}.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    synchronized Collection<String> sendMPDCommand(MPDCommand command) throws MPDConnectionException, MPDResponseException {
        byte[] bytesToSend;
        List<String> responseList = new ArrayList<String>();
        OutputStream outStream = null;
        BufferedReader in;

        if (!socket.isConnected()) {
            try {
                connect();
            } catch (Exception e) {
                throw new MPDConnectionException("Connection to server lost: " + e.getMessage());
            }
        }

        int count = 0;

        Exception excReturn = null;
        while (count < TRIES) {
            try {
                bytesToSend = convertCommand(command.getCommand(), command.getParams()).getBytes(prop.getProperty(MPDPROPSERVERENCODING));

                outStream = socket.getOutputStream();
                outStream.write(bytesToSend);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), prop.getProperty(MPDPROPSERVERENCODING)));

                String inLine;

                while ((inLine = in.readLine()) != null) {
                    if (isResponseOK(inLine)) {
                        //end of command is ok so break
                        break;
                    }

                    if (isResponseError(inLine)) {
                        throw new MPDResponseException(getLastError(), command.getCommand());
                    }

                    responseList.add(inLine);
                }
                count = TRIES + 1;
                return (responseList);
            } catch (Exception e) {
                System.out.println("Got Error from:" + command.getCommand());
                for (String str : command.getParams()) {
                    System.out.println("\tparam:" + str);
                }
                e.printStackTrace();

                if (e instanceof SocketException) {
                    try {
                        connect();
                    } catch (Exception ex) {
                        Logger.getLogger(MPD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    responseList = new ArrayList<String>();
                    ++count;
                    excReturn = e;
                    System.out.println("Retrying command " + count);
                } else {
                    throw new MPDResponseException(e.getMessage());
                }
            } finally {
                try {
                    outStream.flush();
                } catch (IOException ex) {
                    Logger.getLogger(MPD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        throw new MPDConnectionException(excReturn.getMessage());
    }

    /**
     * Sends a list of {@link MPDCommand}s all at once to the MPD server and returns
     * true if all commands were sent successfully.  If any of the commands received
     * as error in the response false will be returned.
     *
     * @param commandList the list of {@link MPDCommand}s
     * @return true if successful, false otherwise
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    synchronized boolean sendMPDCommands(List<MPDCommand> commandList) throws MPDConnectionException, MPDResponseException {
        boolean isOk = true;
        StringBuffer sb;

        sb = new StringBuffer(convertCommand(prop.getProperty(MPDPROPSTARTBULK), new ArrayList<String>()));

        for (MPDCommand command : commandList) {
            sb.append(convertCommand(command.getCommand(), command.getParams()));
        }

        sb.append(convertCommand(prop.getProperty(MPDPROPENDBULK), new ArrayList<String>()));


        byte[] bytesToSend;

        OutputStream outStream;
        BufferedReader in;

        if (!socket.isConnected()) {
            try {
                connect();
            } catch (Exception e) {
                throw new MPDConnectionException("Connection to server lost: " + e.getMessage());
            }
        }

        try {
            bytesToSend = sb.toString().getBytes(prop.getProperty(MPDPROPSERVERENCODING));
            outStream = socket.getOutputStream();
            outStream.write(bytesToSend);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inLine;
            while ((inLine = in.readLine()) != null) {
                if (!inLine.startsWith("list_OK")) {
                    isOk = false;
                }
                if (isResponseOK(inLine)) {
                    //end of command is ok so break
                    break;
                }

                if (isResponseError(inLine)) {
                    throw new MPDResponseException(getLastError());
                }
            }
        } catch (Exception e) {
            throw new MPDConnectionException(e.getMessage());
        }

        return (isOk);
    }

    /**
     * Returns the properties file containing the command list for the MPD.
     *
     * @return the command list properties file
     */
    Properties getMPDProperties() {
        return (prop);
    }

    private void authenticate(String password) throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPPASSWORD), password);
        sendMPDCommand(command);
    }

    /**
     * Returns the current status of the requested status element.
     * See <code>StatusList</code> for a list of possible items returned
     * by getStatus.
     *
     * @param status the status desired
     * @return the desired status information
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    protected String getStatus(StatusList status) throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPSTATUS));
        List<String> respList = new ArrayList<String>(sendMPDCommand(command));

        for (String line : respList) {
            if (line.startsWith(status.getStatusPrefix())) {
                return (line.substring(status.getStatusPrefix().length()).trim());
            }
        }
        return (null);
    }

    /**
     * Returns the statistics of the requested statistics element.
     * See <code>StatList</code> for a list of possible items returned
     * by getServerStat.
     *
     * @param stat the statistic desired
     * @return the requested statistic
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    protected String getServerStat(StatList stat) throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPSTATS));
        List<String> respList = new ArrayList<String>(sendMPDCommand(command));

        for (String line : respList) {
            if (line.startsWith(stat.getStatPrefix())) {
                return (line.substring(stat.getStatPrefix().length()).trim());
            }
        }
        return (null);
    }

    /**
     * Converts the response from the MPD server into a {@link MPDSong} object.
     *
     * @param list the response from the MPD server
     * @return a MPDSong object
     */
    protected Collection<MPDSong> convertResponseToSong(List<String> list) {
        List<MPDSong> songList = new ArrayList<MPDSong>();
        Iterator iter = list.iterator();
        String line = null;

        while (iter.hasNext()) {
            if (line == null || (!line.startsWith(SONGPREFIXFILE))) {
                line = (String) iter.next();
            }

            if (line.startsWith(SONGPREFIXFILE)) {
                MPDSong song = new MPDSong();
                song.setFile(line.substring(SONGPREFIXFILE.length()).trim());
                while (!(line = (String) iter.next()).startsWith(SONGPREFIXFILE)) {
                    if (line.startsWith(SONGPREFIXALBUM)) {
                        song.setAlbum(new MPDAlbum(line.substring(SONGPREFIXALBUM.length()).trim()));
                    } else if (line.startsWith(SONGPREFIXARTIST)) {
                        song.setArtist(new MPDArtist(line.substring(SONGPREFIXARTIST.length()).trim()));
                    } else if (line.startsWith(SONGPREFIXTIME)) {
                        song.setLength(Integer.parseInt(line.substring(SONGPREFIXTIME.length()).trim()));
                    } else if (line.startsWith(SONGPREFIXTITLE)) {
                        song.setTitle(line.substring(SONGPREFIXTITLE.length()).trim());
                    } else if (line.startsWith(SONGPREFIXDATE)) {
                        song.setYear(line.substring(SONGPREFIXDATE.length()).trim());
                    } else if (line.startsWith(SONGPREFIXGENRE)) {
                        song.setGenre(line.substring(SONGPREFIXGENRE.length()).trim());
                    } else if (line.startsWith(SONGPREFIXCOMMENT)) {
                        song.setComment(line.substring(SONGPREFIXCOMMENT.length()).trim());
                    } else if (line.startsWith(SONGPREFIXTRACK)) {
                        try {
                            song.setTrack(Integer.parseInt(line.substring(SONGPREFIXTRACK.length()).trim().split("/")[0]));
                        } catch (NumberFormatException nfe) {
                            song.setTrack(0);
                        }
                    } else if (line.startsWith(SONGPREFIXPOS)) {
                        song.setPosition(Integer.parseInt(line.substring(SONGPREFIXPOS.length()).trim()));
                    } else if (line.startsWith(SONGPREFIXID)) {
                        song.setId(Integer.parseInt(line.substring(SONGPREFIXID.length()).trim()));
                    } else if (line.startsWith(SONGPREFIXDISC)) {
                        song.setDiscNumber(line.substring(SONGPREFIXDISC.length()).trim());
                    }
                    if (!iter.hasNext()) {
                        break;
                    }
                }
                songList.add(song);
            }
        }
        return (songList);
    }

    private boolean isResponseOK(final String line) {
        try {
            if (line.startsWith(prop.getProperty(MPDPROPRESPONSEOK))) {
                return (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (false);
    }

    private boolean isResponseError(final String line) {
        try {
            if (line.startsWith(prop.getProperty(MPDPROPRESPONSEERR))) {
                this.lastError = line.substring(prop.getProperty(MPDPROPRESPONSEERR).length()).trim();
                return (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (false);
    }

    /**
     * If MPD is already connected no attempt will be made to connect and the
     * version is returned.
     * <p/>
     * A timeout of 0 means an infinite wait.
     *
     * @param timeout socket timeout, 0 for infinite wait
     * @return the version of MPD
     * @throws java.io.IOException if there is a socked io problem
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                             if there are connection issues
     */
    private synchronized String connect(int timeout) throws IOException, MPDConnectionException {
        BufferedReader in;

        this.socket = new Socket();
        SocketAddress sockaddr = new InetSocketAddress(serverAddress, port);
        try {
            this.socket.connect(sockaddr, timeout);
        } catch (SocketTimeoutException ste) {
            throw new MPDTimeoutException(ste);
        }
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = in.readLine();
        if (isResponseOK(line)) {
            return (stripResponse(Response.OK, line).trim());
        } else {
            throw new MPDConnectionException("Response from server: " + stripResponse(Response.ERR, line));
        }
    }

    /**
     * Attempts to connect to MPD with an infinite timeout value.
     * If MPD is already connected no attempt will be made to connect and the
     * version is returned.
     *
     * @return return the version of MPD
     * @throws java.io.IOException if there is a socked io problem
     * @throws org.bff.javampd.exception.MPDConnectionException
     *                             if there are connection issues
     */
    private synchronized String connect() throws IOException, MPDConnectionException {
        return connect(0);
    }

    private boolean ping() {
        try {
            sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPPING)));
        } catch (MPDException e) {
            return (false);
        }
        return (true);
    }

    private String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        if (params != null) {
            for (String param : params) {
                if (param != null) {
                    sb.append(param.contains(" ") ? " \"" : " ");
                    sb.append(param);
                    sb.append(param.contains(" ") ? "\"" : "");
                }
            }
            sb.append("\n");
        }

        return (sb.toString());
    }

    private String stripResponse(Response response, String line) {
        return (line.substring(prop.getProperty(response.getProp()).length()));
    }

    private void loadMpdPropValues() {
        prop = new Properties();

        InputStream is = getClass().getResourceAsStream(PROPFILE);

        try {
            prop.load(is);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem with properties file, make sure exists and in path.");
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MPD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns the last error generated by <code>isResponseOK</code>.
     *
     * @return the error message
     */
    protected String getLastError() {
        return lastError;
    }
}
