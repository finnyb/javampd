package org.bff.javampd.monitor;

import org.bff.javampd.MPDAdmin;
import org.bff.javampd.MPDOutput;
import org.bff.javampd.events.OutputChangeEvent;
import org.bff.javampd.events.OutputChangeListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDOutputMonitorTest {
    private boolean success;

    @Mock
    private MPDAdmin mpdAdmin;

    @InjectMocks
    private MPDOutputMonitor outputMonitor;


    @Before
    public void setUp() {
        success = false;
    }

    @Test
    public void testCheckStatusOutputAdded() throws Exception {
        outputMonitor.addOutputChangeListener(new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success =
                        event.getEvent() == OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
            }
        });
        MPDOutput output = new MPDOutput(1);
        List<MPDOutput> outputList = new ArrayList<>();
        outputList.add(output);
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        assertTrue(success);
    }

    @Test
    public void testCheckStatusOutputDeleted() throws Exception {
        outputMonitor.addOutputChangeListener(new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success =
                        event.getEvent() == OutputChangeEvent.OUTPUT_EVENT.OUTPUT_DELETED;
            }
        });
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);
        List<MPDOutput> outputList = new ArrayList<>();
        outputList.add(output1);
        outputList.add(output2);

        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        outputList = new ArrayList<>();
        outputList.add(output1);
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        assertTrue(success);
    }

    @Test
    public void testCheckStatusOutputChanged() throws Exception {
        outputMonitor.addOutputChangeListener(new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success =
                        event.getEvent() == OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED;
            }
        });
        MPDOutput output1 = new MPDOutput(1);
        List<MPDOutput> outputList = new ArrayList<>();
        outputList.add(output1);

        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        outputList = new ArrayList<>();
        output1 = new MPDOutput(1);
        output1.setEnabled(true);
        outputList.add(output1);
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        MPDOutput output = new MPDOutput(1);
        List<MPDOutput> outputList = new ArrayList<>();
        outputList.add(output);
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        outputMonitor.addOutputChangeListener(new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success =
                        event.getEvent() == OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED;
            }
        });
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        OutputChangeListener trackPositionChangeListener = new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success = true;
            }
        };

        outputMonitor.addOutputChangeListener(trackPositionChangeListener);

        MPDOutput output = new MPDOutput(1);
        List<MPDOutput> outputList = new ArrayList<>();
        outputList.add(output);
        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();

        assertTrue(success);

        success = false;

        outputMonitor.removeOutputChangedListener(trackPositionChangeListener);
        MPDOutput output2 = new MPDOutput(2);
        outputList.add(output2);

        when(mpdAdmin.getOutputs()).thenReturn(outputList);
        outputMonitor.checkStatus();
        assertFalse(success);
    }
}
