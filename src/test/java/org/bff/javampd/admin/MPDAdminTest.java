package org.bff.javampd.admin;

import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.statistics.ServerStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private ArgumentCaptor<Integer> integerParamArgumentCaptor;

    @Captor
    private ArgumentCaptor<String[]> stringParamArgumentCaptor;

    @InjectMocks
    private MPDAdmin admin;

    private AdminProperties realAdminProperties;

    @BeforeEach
    public void before() {
        realAdminProperties = new AdminProperties();
    }

    @Test
    public void getOutputs() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());

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
    public void testAddChangeListener() {
        final MPDChangeEvent[] eventReceived = new MPDChangeEvent[1];

        MPDChangeEvent.Event changeEvent = MPDChangeEvent.Event.KILLED;
        MPDChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertEquals(changeEvent, eventReceived[0].getEvent());
    }

    @Test
    public void testRemoveListener() {
        final MPDChangeEvent[] eventReceived = new MPDChangeEvent[1];

        MPDChangeEvent.Event changeEvent = MPDChangeEvent.Event.KILLED;
        MPDChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertEquals(changeEvent, eventReceived[0].getEvent());

        eventReceived[0] = null;
        admin.removeMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertNull(eventReceived[0]);
    }

    @Test
    public void testAddOutputChangeListener() {
        final OutputChangeEvent[] eventReceived = new OutputChangeEvent[1];

        OutputChangeEvent.OUTPUT_EVENT changeEvent = OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
        OutputChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addOutputChangeListener(changeListener);
        admin.fireOutputChangeEvent(changeEvent);

        assertEquals(changeEvent, eventReceived[0].getEvent());
    }

    @Test
    public void testRemoveOutputListener() {
        final OutputChangeEvent[] eventReceived = new OutputChangeEvent[1];

        OutputChangeEvent.OUTPUT_EVENT changeEvent = OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
        OutputChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addOutputChangeListener(changeListener);
        admin.fireOutputChangeEvent(changeEvent);

        assertEquals(changeEvent, eventReceived[0].getEvent());

        eventReceived[0] = null;
        admin.removeOutputChangeListener(changeListener);
        admin.fireOutputChangeEvent(changeEvent);

        assertNull(eventReceived[0]);
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
