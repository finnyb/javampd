/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd;

import org.bff.javampd.server.MPDConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Bill
 */
public class TestProperties {
    private static final String PROP_SERVER = "server";
    private static final String PROP_PATH = "path.testmp3";
    private static final String PROP_SERVER_PATH = "path.server.mp3";
    private static final String FILE_PROPS = System.getProperty("user.dir") + "/src/it/resources/TestProperties.properties";
    private static final String PROP_VERSION = "mpd.version";
    private static final String PROP_PASSWORD = "password";
    private static String version;
    private static final String PROP_PORT = "port";
    private static TestProperties instance;

    private String path;
    private String server;
    private int port;
    private String serverPath;
    private String password;

    private TestProperties() throws IOException, MPDConnectionException {
        loadProperties();
    }

    public static TestProperties getInstance() throws IOException, MPDConnectionException {
        if (instance == null) {
            instance = new TestProperties();
        }

        return instance;
    }

    private void loadProperties() throws IOException {
        Properties props = new Properties();

        InputStream in = null;
        try {
            in = new FileInputStream(FILE_PROPS);
            props.load(in);
            this.server = props.getProperty(PROP_SERVER);
            this.path = props.getProperty(PROP_PATH);
            this.port = Integer.parseInt(props.getProperty(PROP_PORT));
            this.serverPath = props.getProperty(PROP_SERVER_PATH);
            this.password = props.getProperty(PROP_PASSWORD);
            this.version = props.getProperty(PROP_VERSION);
        } finally {
            assert in != null;
            in.close();
        }
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
}
