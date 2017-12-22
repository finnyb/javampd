package org.bff.javampd.monitor;

import org.bff.javampd.admin.Admin;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDOutputMonitorTest {

    @Mock
    private Admin admin;

    @InjectMocks
    private MPDOutputMonitor outputMonitor;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddOutputChangeListener() throws Exception {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(new MPDOutput(0));
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());
    }

    @Test
    public void testRemoveOutputChangeListener() throws Exception {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        OutputChangeListener outputChangeListener = event -> outputEvent[0] = event;

        outputMonitor.addOutputChangeListener(outputChangeListener);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(new MPDOutput(0));
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());

        outputEvent[0] = null;
        outputMonitor.removeOutputChangeListener(outputChangeListener);
        outputMonitor.checkStatus();
        assertNull(outputEvent[0]);
    }

    @Test
    public void testOutputAdded() throws Exception {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(new MPDOutput(0));
        testOutputs.add(new MPDOutput(1));
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());
    }

    @Test
    public void testOutputRemoved() throws Exception {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(new MPDOutput(0));
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        testOutputs = new ArrayList<>();
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_DELETED, outputEvent[0].getEvent());
    }

    @Test
    public void testOutputChanged() throws Exception {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(new MPDOutput(0));
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        testOutputs = new ArrayList<>();
        MPDOutput output = new MPDOutput(0);
        output.setEnabled(true);
        testOutputs.add(output);
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED, outputEvent[0].getEvent());
    }
}