package org.bff.javampd.admin;

import org.bff.javampd.BaseTest;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDResponseException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MPDAdminIT extends BaseTest {
    private Admin admin;

    @Before
    public void setup() throws Exception {
        this.admin = getMpd().getAdmin();
    }

    @AfterClass
    public static void after() throws MPDAdminException {
        MPDOutput output = new ArrayList<>(getMpd().getAdmin().getOutputs()).get(0);
        Assert.assertTrue(getMpd().getAdmin().enableOutput(output));
    }

    @Test
    public void disableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        Assert.assertTrue(admin.disableOutput(output));
        output = new ArrayList<>(admin.getOutputs()).get(0);
        Assert.assertFalse(output.isEnabled());
    }

    @Test
    public void enableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        Assert.assertTrue(admin.enableOutput(output));
        output = new ArrayList<>(admin.getOutputs()).get(0);
        Assert.assertTrue(output.isEnabled());
    }

    @Test
    public void testOutputs() throws MPDConnectionException, MPDResponseException {
        List<MPDOutput> outputs = new ArrayList<MPDOutput>(admin.getOutputs());
        Assert.assertTrue(outputs.size() == 1);
        MPDOutput output = outputs.get(0);
        Assert.assertTrue(0 == output.getId());
        Assert.assertTrue(!"".equals(output.getName()));
    }
}
