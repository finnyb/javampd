package org.bff.javampd.monitor;

import org.bff.javampd.admin.Admin;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDOutputMonitorTest {

    @Mock
    private Admin admin;

    @InjectMocks
    private MPDOutputMonitor outputMonitor;

    @Test
    void testAddOutputChangeListener() {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(MPDOutput.builder(0).build());
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());
    }

    @Test
    void testRemoveOutputChangeListener() {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        OutputChangeListener outputChangeListener = event -> outputEvent[0] = event;

        outputMonitor.addOutputChangeListener(outputChangeListener);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(MPDOutput.builder(0).build());
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());

        outputEvent[0] = null;
        outputMonitor.removeOutputChangeListener(outputChangeListener);
        outputMonitor.checkStatus();
        assertNull(outputEvent[0]);
    }

    @Test
    void testOutputAdded() {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(MPDOutput.builder(0).build());
        testOutputs.add(MPDOutput.builder(1).build());
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();
        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED, outputEvent[0].getEvent());
    }

    @Test
    void testOutputRemoved() {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(MPDOutput.builder(0).build());
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        testOutputs = new ArrayList<>();
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_DELETED, outputEvent[0].getEvent());
    }

    @Test
    void testOutputChanged() {
        final OutputChangeEvent[] outputEvent = new OutputChangeEvent[1];

        outputMonitor.addOutputChangeListener(event -> outputEvent[0] = event);
        List<MPDOutput> testOutputs = new ArrayList<>();
        testOutputs.add(MPDOutput.builder(0).build());
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        testOutputs = new ArrayList<>();
        MPDOutput output = MPDOutput.builder(0).build();
        output.setEnabled(true);
        testOutputs.add(output);
        when(admin.getOutputs()).thenReturn(testOutputs);
        outputMonitor.checkStatus();

        assertEquals(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED, outputEvent[0].getEvent());
    }
}
