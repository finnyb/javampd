package org.bff.javampd.admin;

import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDAdminTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

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
    private MPDAdmin admin;

    private AdminProperties realAdminProperties;

    @Before
    public void before() {
        realAdminProperties = new AdminProperties();
    }

    @Test
    public void getOutputs() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand(adminProperties.getOutputs())).thenReturn(getOutputResponse());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        assertEquals(output.getName(), "My ALSA Device");
        assertEquals(output.getId(), 0);

        output = new ArrayList<>(admin.getOutputs()).get(1);
        assertEquals(output.getName(), "My ALSA Device 2");
        assertEquals(output.getId(), 0);
    }

    @Test
    public void disableOutput() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputDisable()).thenReturn(realAdminProperties.getOutputDisable());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputDisable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void enableOutput() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputEnable()).thenReturn(realAdminProperties.getOutputEnable());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.enableOutput(output);
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertEquals(adminProperties.getOutputEnable(), commandArgumentCaptor.getValue());
        assertEquals(0, integerParamArgumentCaptor.getValue());
    }

    @Test
    public void killMPD() {
        when(adminProperties.getKill()).thenReturn(realAdminProperties.getKill());

        admin.killMPD();
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getKill(), commandArgumentCaptor.getValue());
    }

    @Test
    public void updateDatabase() {
        when(adminProperties.getRefresh()).thenReturn(realAdminProperties.getRefresh());

        admin.updateDatabase();
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
    }

    @Test
    public void updateDatabaseWithPath() {
        admin.updateDatabase("testPath");
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), stringParamArgumentCaptor.capture());
        assertEquals(adminProperties.getRefresh(), commandArgumentCaptor.getValue());
        assertEquals("testPath", stringParamArgumentCaptor.getValue());
    }

    @Test
    public void getDaemonUpTime() {
        admin.getDaemonUpTime();
        verify(serverStatistics, times(1)).getUptime();
    }

    @Test
    public void testAddMPDChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        MPDChangeListener mcl = event -> gotEvent.set(true);

        admin.addMPDChangeListener(mcl);
        admin.fireMPDChangeEvent(MPDChangeEvent.Event.MPD_KILLED);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testRemoveMPDChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        MPDChangeListener mcl = event -> gotEvent.set(true);

        admin.addMPDChangeListener(mcl);
        admin.fireMPDChangeEvent(MPDChangeEvent.Event.MPD_KILLED);
        assertTrue(gotEvent.get());

        gotEvent.set(false);
        admin.removePlayerChangedListener(mcl);
        admin.fireMPDChangeEvent(MPDChangeEvent.Event.MPD_KILLED);
        assertFalse(gotEvent.get());
    }

    @Test
    public void testFireMPDChangeEvent() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        MPDChangeListener mcl = event -> gotEvent.set(true);

        admin.addMPDChangeListener(mcl);
        admin.fireMPDChangeEvent(MPDChangeEvent.Event.MPD_REFRESHED);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testAddOutputChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        OutputChangeListener ocl = event -> gotEvent.set(true);

        admin.addOutputChangeListener(ocl);
        admin.fireOutputChangeEvent(OutputChangeEvent.Event.OUTPUT_CHANGED);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testRemoveOutputChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        OutputChangeListener ocl = event -> gotEvent.set(true);

        admin.addOutputChangeListener(ocl);
        admin.fireOutputChangeEvent(OutputChangeEvent.Event.OUTPUT_ADDED);
        assertTrue(gotEvent.get());

        gotEvent.set(false);
        admin.removeOutputChangeListener(ocl);
        admin.fireOutputChangeEvent(OutputChangeEvent.Event.OUTPUT_DELETED);
        assertFalse(gotEvent.get());
    }

    @Test
    public void testFireOutputChangeEvent() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        OutputChangeListener ocl = event -> gotEvent.set(true);

        admin.addOutputChangeListener(ocl);
        admin.fireOutputChangeEvent(OutputChangeEvent.Event.OUTPUT_ADDED);
        assertTrue(gotEvent.get());
    }

    private List<String> getOutputResponse() {
        List<String> responseList = new ArrayList<>();
        responseList.add("outputid: 0");
        responseList.add("outputname: My ALSA Device");
        responseList.add("outputenabled: 0");

        responseList.add("outputid: 1");
        responseList.add("outputname: My ALSA Device 2");
        responseList.add("outputenabled: 0");

        return responseList;
    }
}
