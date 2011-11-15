package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.exception.MPDTimeoutException;
import org.junit.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPDTest extends BaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testVersion() {
        Assert.assertEquals(Controller.getVersion(), getMpd().getVersion());
    }

    @Test
    public void testPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = new MPD(Controller.getInstance().getServer(),
                    Controller.getInstance().getPort(),
                    Controller.getInstance().getPassword());
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Test(expected = MPDConnectionException.class)
    public void testWrongPassword() throws IOException, MPDConnectionException {
        MPD mpd = null;
        try {
            mpd = new MPD(Controller.getInstance().getServer(),
                    Controller.getInstance().getPort(),
                    Controller.getInstance().getPassword() + "WRONG");
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Test(expected = MPDTimeoutException.class)
    public void testTimeout() throws MPDException, IOException {
        MPD mpd = null;
        try {
            mpd = new MPD("10.255.255.1",
                    Controller.getInstance().getPort(), 500);
        } finally {
            if (mpd != null) {
                try {
                    mpd.close();
                } catch (MPDResponseException ex) {
                    Logger.getLogger(MPDTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
