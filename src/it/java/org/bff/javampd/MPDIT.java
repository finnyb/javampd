package org.bff.javampd;

import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDSecurityException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDIT extends BaseTest {

    @Test
    public void testVersion() {
        Assert.assertEquals(TestProperties.getVersion(), getMpd().getVersion());
    }

    @Test
    public void testDefaultMPD() throws MPDConnectionException {
        MPD mpd = new MPD.Builder().build();
        assertTrue(mpd.isConnected());
    }

    @Test
    public void testClearError() throws IOException, MPDException {
        MPD mpd = null;
        try {
            mpd = getNewMpd();
            mpd.clearError();
        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test
    public void testPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = getNewMpd();
        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test(expected = MPDConnectionException.class)
    public void testWrongPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = getNewMpd(TestProperties.getInstance().getPassword() + "WRONG");
        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test(expected = MPDSecurityException.class)
    public void testNoPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .build();
            MPDCommandExecutor mpdCommandExecutor = new MPDCommandExecutor();
            mpdCommandExecutor.setMpd(mpd);
            mpdCommandExecutor.sendCommand("list", TagLister.ListType.ARTIST.getType());

        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test(expected = MPDSecurityException.class)
    public void testNoPasswordCommandList() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .build();

            MPDCommandExecutor mpdCommandExecutor = new MPDCommandExecutor();
            mpdCommandExecutor.setMpd(mpd);
            List<MPDCommand> commandList = new ArrayList<>();
            MPDCommand mpdCommand = new MPDCommand("list", TagLister.ListType.ARTIST.getType());
            MPDCommand mpdCommand2 = new MPDCommand("list", TagLister.ListType.GENRE.getType());
            commandList.add(mpdCommand);
            commandList.add(mpdCommand2);

            mpdCommandExecutor.sendCommands(commandList);

        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test(expected = MPDConnectionException.class)
    public void testTimeout() throws Exception {
        MPD mpd = null;
        TestProperties testProperties = TestProperties.getInstance();
        try {
            mpd = new MPD.Builder()
                    .server("10.255.255.1")
                    .port(testProperties.getPort())
                    .timeout(500)
                    .build();
            mpd.getVersion();
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (Exception e) {
                    //dont care
                }
            }
        }
    }

    @Test
    public void testIsConnected() throws Exception {
        MPD mpd = null;
        try {
            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .build();

            assertTrue(mpd.isConnected());
        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }

    @Test
    public void testIsConnectedAfterClose() throws Exception {
        MPD mpd = new MPD.Builder()
                .server(TestProperties.getInstance().getServer())
                .port(TestProperties.getInstance().getPort())
                .build();
        mpd.close();

        assertFalse(mpd.isConnected());
    }

    @Test
    public void testNewConnectionAfterClose() throws Exception {
        MPD mpd = null;
        try {
            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .build();
            mpd.close();

            assertFalse(mpd.isConnected());

            mpd = new MPD.Builder()
                    .server(TestProperties.getInstance().getServer())
                    .port(TestProperties.getInstance().getPort())
                    .build();
            assertTrue(mpd.isConnected());
        } finally {
            if (mpd != null) {
                mpd.close();
            }
        }
    }
}
