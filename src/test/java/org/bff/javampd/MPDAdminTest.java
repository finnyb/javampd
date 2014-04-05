package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.AdminProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDAdminTest {

    @Mock
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private AdminProperties adminProperties;

    @Mock
    private ServerStatistics serverStatistics;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer[]> integerParamArgumentCaptor;

    @Captor
    private ArgumentCaptor<String[]> stringParamArgumentCaptor;

    @InjectMocks
    private MPDAdmin mpdAdmin;

    @Before
    public void before() throws MPDResponseException {
        setUpMocks();
    }

    @Test
    public void getOutputs() throws MPDException {
        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        assertEquals(output.getName(), "My ALSA Device");
        assertEquals(output.getId(), 0);
    }

    @Test
    public void disableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.disableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputDisable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void enableOutput() throws MPDConnectionException, MPDResponseException {
        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.enableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputEnable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void killMPD() throws MPDException {
        mpdAdmin.killMPD();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getKill(), commandArgumentCaptor.getValue());
    }

    @Test
    public void updateDatabase() throws MPDException {
        mpdAdmin.updateDatabase();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
    }

    @Test
    public void updateDatabaseWithPath() throws MPDException {
        mpdAdmin.updateDatabase("testPath");
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), stringParamArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
        assertEquals("testPath", stringParamArgumentCaptor.getValue());
    }

    @Test
    public void getDaemonUpTime() throws MPDException {
        mpdAdmin.getDaemonUpTime();
        verify(serverStatistics, times(1)).getUptime();
    }

    private List<String> getOutputResponse() {
        List<String> responseList = new ArrayList<>();
        responseList.add("outputid: 0");
        responseList.add("outputname: My ALSA Device");
        responseList.add("outputenabled: 0");

        return responseList;
    }

    private void setUpMocks() throws MPDResponseException {
        when(mpdCommandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());

        AdminProperties realAdminProperties = new AdminProperties();

        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(adminProperties.getKill()).thenReturn(realAdminProperties.getKill());
        when(adminProperties.getOutputDisable()).thenReturn(realAdminProperties.getOutputDisable());
        when(adminProperties.getOutputEnable()).thenReturn(realAdminProperties.getOutputEnable());
        when(adminProperties.getRefresh()).thenReturn(realAdminProperties.getRefresh());
    }
}
