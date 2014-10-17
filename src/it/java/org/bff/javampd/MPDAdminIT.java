package org.bff.javampd;

import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MPDAdminIT extends BaseTest {

    @After
    public void after() throws MPDAdminException {
        MPDOutput output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        getAdmin().enableOutput(output);
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
