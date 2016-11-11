package org.bff.javampd.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerPropertiesTest {
    private ServerProperties serverProperties;

    @Before
    public void setUp() throws Exception {
        serverProperties = new ServerProperties();
    }

    @Test
    public void getClearError() throws Exception {
        assertEquals("clearerror", serverProperties.getClearError());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals("status", serverProperties.getStatus());
    }

    @Test
    public void getStats() throws Exception {
        assertEquals("stats", serverProperties.getStats());
    }

    @Test
    public void getPing() throws Exception {
        assertEquals("ping", serverProperties.getPing());
    }

    @Test
    public void getPassword() throws Exception {
        assertEquals("password", serverProperties.getPassword());
    }

    @Test
    public void getClose() throws Exception {
        assertEquals("close", serverProperties.getClose());
    }

    @Test
    public void getStartBulk() throws Exception {
        assertEquals("command_list_ok_begin", serverProperties.getStartBulk());
    }

    @Test
    public void getEndBulk() throws Exception {
        assertEquals("command_list_end", serverProperties.getEndBulk());
    }

    @Test
    public void getEncoding() throws Exception {
        assertEquals("UTF-8", serverProperties.getEncoding());
    }

}