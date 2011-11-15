package org.bff.javampd;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void testRemoveSlashes() throws Exception {
        String leading = "/leading";
        String trailing = "trailing/";
        String both = "/both/";

        Assert.assertEquals("leading", Utils.removeSlashes(leading));
        Assert.assertEquals("trailing", Utils.removeSlashes(trailing));
        Assert.assertEquals("both", Utils.removeSlashes(both));
    }
}
