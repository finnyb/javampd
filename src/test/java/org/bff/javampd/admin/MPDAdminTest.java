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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MPDAdminTest {

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
    private ArgumentCaptor<String> stringParamArgumentCaptor;

    @InjectMocks
    private MPDAdmin admin;

    private AdminProperties realAdminProperties;

    @BeforeEach
    void before() {
        realAdminProperties = new AdminProperties();
    }

    @Test
    void getOutputs() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        assertThat(output.getName(), is(equalTo("My ALSA Device")));
        assertThat(output.getId(), is(equalTo(0)));

        output = new ArrayList<>(admin.getOutputs()).get(1);
        assertThat(output.getName(), is(equalTo("My ALSA Device 2")));
        assertThat(output.getId(), is(equalTo(0)));
    }

    @Test
    void disableOutput() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputDisable()).thenReturn(realAdminProperties.getOutputDisable());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertThat(adminProperties.getOutputDisable(), is(equalTo(commandArgumentCaptor.getValue())));
        assertThat(0, is(equalTo(integerParamArgumentCaptor.getValue())));
    }

    @Test
    void enableOutput() {
        when(adminProperties.getOutputs()).thenReturn(realAdminProperties.getOutputs());
        when(commandExecutor.sendCommand("outputs")).thenReturn(getOutputResponse());
        when(adminProperties.getOutputEnable()).thenReturn(realAdminProperties.getOutputEnable());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.enableOutput(output);
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), integerParamArgumentCaptor.capture());
        assertThat(adminProperties.getOutputEnable(), is(equalTo(commandArgumentCaptor.getValue())));
        assertThat(0, is(equalTo(integerParamArgumentCaptor.getValue())));
    }

    @Test
    void killMPD() {
        when(adminProperties.getKill()).thenReturn(realAdminProperties.getKill());

        admin.killMPD();
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertThat(adminProperties.getKill(), is(equalTo(commandArgumentCaptor.getValue())));
    }

    @Test
    void updateDatabase() {
        when(adminProperties.getRefresh()).thenReturn(realAdminProperties.getRefresh());

        admin.updateDatabase();
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertThat(adminProperties.getRefresh(), is(equalTo(commandArgumentCaptor.getValue())));
    }

    @Test
    void rescanDatabase() {
        when(adminProperties.getRescan()).thenReturn(realAdminProperties.getRescan());

        admin.rescanDatabase();
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertThat(adminProperties.getRescan(), is(equalTo(commandArgumentCaptor.getValue())));
    }

    @Test
    void updateDatabaseWithPath() {
        admin.updateDatabase("testPath");
        verify(commandExecutor).sendCommand(commandArgumentCaptor.capture(), stringParamArgumentCaptor.capture());
        assertThat(adminProperties.getRefresh(), is(equalTo(commandArgumentCaptor.getValue())));
        assertThat("testPath", is(equalTo(stringParamArgumentCaptor.getValue())));
    }

    @Test
    void getDaemonUpTime() {
        admin.getDaemonUpTime();
        verify(serverStatistics, times(1)).getUptime();
    }

    @Test
    void testAddChangeListener() {
        final MPDChangeEvent[] eventReceived = new MPDChangeEvent[1];

        MPDChangeEvent.Event changeEvent = MPDChangeEvent.Event.KILLED;
        MPDChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertThat(changeEvent, is(equalTo(eventReceived[0].getEvent())));
    }

    @Test
    void testRemoveListener() {
        final MPDChangeEvent[] eventReceived = new MPDChangeEvent[1];

        MPDChangeEvent.Event changeEvent = MPDChangeEvent.Event.KILLED;
        MPDChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertThat(changeEvent, is(equalTo(eventReceived[0].getEvent())));

        eventReceived[0] = null;
        admin.removeMPDChangeListener(changeListener);
        admin.fireMPDChangeEvent(changeEvent);

        assertNull(eventReceived[0]);
    }

    @Test
    void testAddOutputChangeListener() {
        final OutputChangeEvent[] eventReceived = new OutputChangeEvent[1];

        OutputChangeEvent.OUTPUT_EVENT changeEvent = OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
        OutputChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addOutputChangeListener(changeListener);
        admin.fireOutputChangeEvent(changeEvent);

        assertThat(changeEvent, is(equalTo(eventReceived[0].getEvent())));
    }

    @Test
    void testRemoveOutputListener() {
        final OutputChangeEvent[] eventReceived = new OutputChangeEvent[1];

        OutputChangeEvent.OUTPUT_EVENT changeEvent = OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
        OutputChangeListener changeListener = event -> eventReceived[0] = event;

        admin.addOutputChangeListener(changeListener);
        admin.fireOutputChangeEvent(changeEvent);

        assertThat(changeEvent, is(equalTo(eventReceived[0].getEvent())));

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
