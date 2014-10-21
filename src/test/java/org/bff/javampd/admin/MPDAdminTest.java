package org.bff.javampd.admin;

import org.bff.javampd.MPDException;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDResponseException;
import org.bff.javampd.statistics.ServerStatistics;
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

    private AdminProperties realAdminProperties;
    @Before
    public void before() throws MPDResponseException {
        realAdminProperties = new AdminProperties();
    }

    @Test
    public void getOutputs() throws MPDException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(mpdCommandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        assertEquals(output.getName(), "My ALSA Device");
        assertEquals(output.getId(), 0);
    }

    @Test(expected = MPDAdminException.class)
    public void getOutputsException() throws MPDException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(mpdCommandExecutor.sendCommand("outputs")).thenThrow(new MPDResponseException());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        assertEquals(output.getName(), "My ALSA Device");
        assertEquals(output.getId(), 0);
    }

    @Test
    public void disableOutput() throws MPDConnectionException, MPDResponseException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(mpdCommandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputDisable()).thenReturn(realAdminProperties.getOutputDisable());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.disableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputDisable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test(expected = MPDAdminException.class)
    public void disableOutputException() throws MPDConnectionException, MPDResponseException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(adminProperties.getOutputDisable()).thenReturn(realAdminProperties.getOutputDisable());
        when(mpdCommandExecutor.sendCommand("outputs")).thenThrow(new MPDResponseException());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.disableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputDisable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void enableOutput() throws MPDConnectionException, MPDResponseException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(mpdCommandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputEnable()).thenReturn(realAdminProperties.getOutputEnable());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.enableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputEnable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test(expected = MPDAdminException.class)
    public void enableOutputException() throws MPDConnectionException, MPDResponseException {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(mpdCommandExecutor.sendCommand("outputs")).thenThrow(new MPDResponseException());
        when(adminProperties.getOutputEnable()).thenReturn(realAdminProperties.getOutputEnable());

        MPDOutput output = new ArrayList<>(mpdAdmin.getOutputs()).get(0);
        mpdAdmin.enableOutput(output);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputEnable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void killMPD() throws MPDException {
        when(adminProperties.getKill()).thenReturn(realAdminProperties.getKill());

        mpdAdmin.killMPD();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getKill(), commandArgumentCaptor.getValue());
    }

    @Test(expected = MPDAdminException.class)
    public void killMPDException() throws MPDException {
        when(mpdCommandExecutor.sendCommand(adminProperties.getKill())).thenThrow(new MPDResponseException());

        mpdAdmin.killMPD();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getKill(), commandArgumentCaptor.getValue());
    }

    @Test
    public void updateDatabase() throws MPDException {
        when(adminProperties.getRefresh()).thenReturn(realAdminProperties.getRefresh());

        mpdAdmin.updateDatabase();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
    }

    @Test(expected = MPDAdminException.class)
    public void updateDatabaseException() throws MPDException {
        when(mpdCommandExecutor.sendCommand(adminProperties.getRefresh())).thenThrow(new MPDResponseException());

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

    @Test(expected = MPDAdminException.class)
    public void updateDatabaseWithPathException() throws MPDException {
        String testPath = "testPath";

        when(mpdCommandExecutor.sendCommand(adminProperties.getRefresh(), testPath)).thenThrow(new MPDResponseException());

        mpdAdmin.updateDatabase(testPath);
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), stringParamArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
        assertEquals("testPath", stringParamArgumentCaptor.getValue());
    }

    @Test
    public void getDaemonUpTime() throws MPDException {
        mpdAdmin.getDaemonUpTime();
        verify(serverStatistics, times(1)).getUptime();
    }

    @Test(expected = MPDAdminException.class)
    public void getDaemonUpTimeException() throws MPDException {
        when(serverStatistics.getUptime()).thenThrow(new MPDResponseException());

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
}
