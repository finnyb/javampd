package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPDIT extends BaseTest {

    @Test
    public void testVersion() {
        Assert.assertEquals(TestProperties.getVersion(), getMpd().getVersion());
    }

    @Test
    public void testPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = getNewMpd();
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDIT.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDIT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Test(expected = MPDConnectionException.class)
    public void testTimeout() throws MPDException, IOException {
        MPD mpd = null;
        TestProperties testProperties = TestProperties.getInstance();
        try {
            mpd = new MPD.Builder()
                    .server("10.255.255.1")
                    .port(testProperties.getPort())
                    .timeout(500)
                    .build();
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDIT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
