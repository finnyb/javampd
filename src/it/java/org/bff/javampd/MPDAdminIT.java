package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class MPDAdminIT extends BaseTest {

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
    public void disableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        Assert.assertTrue(getAdmin().disableOutput(output));
        output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        Assert.assertFalse(output.isEnabled());
    }

    @Test
    public void enableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        Assert.assertTrue(getAdmin().enableOutput(output));
        output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        Assert.assertTrue(output.isEnabled());
    }

    @Test
    public void testOutputs() throws MPDConnectionException, MPDResponseException {
        List<MPDOutput> outputs = new ArrayList<MPDOutput>(getAdmin().getOutputs());
        Assert.assertTrue(outputs.size() == 1);
        MPDOutput output = outputs.get(0);
        Assert.assertTrue(0 == output.getId());
        Assert.assertTrue(!"".equals(output.getName()));
    }
}
