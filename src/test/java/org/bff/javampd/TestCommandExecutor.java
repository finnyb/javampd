package org.bff.javampd;

/**
 * @author bill
 * @since: 11/23/13 6:22 PM
 */
public class TestCommandExecutor extends MPDCommandExecutor {
    private MPDSocket mpdSocket;

    public TestCommandExecutor() {
    }


    public MPDSocket getMpdSocket() {
        return mpdSocket;
    }

    public void setMPDSocket(MPDSocket mpdSocket) {
        this.mpdSocket = mpdSocket;
    }
}
