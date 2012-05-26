package org.bff.javampd.mock;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDCommand;
import org.bff.javampd.MockUtils;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockMPD extends MPD {
    public static final String TEST_FILE = "src/test/resources/testdata.txt";
    private List<TestData> testDataList;
    private String version;


    public MockMPD(String server, int port, String password) {
        loadTestFile();
    }

    public MockMPD(String server, int port, int timeout) {
        loadTestFile();
    }

    protected synchronized String connect(int timeout) throws IOException, MPDConnectionException {
        return version;
    }

    @Override
    public Collection<String> sendMPDCommand(MPDCommand command) throws MPDResponseException, MPDConnectionException {
        return lookupResponse(command);
    }

    public boolean sendMPDCommands(List<MPDCommand> commandList) {
        return true;
    }

    private void loadTestFile() {
        testDataList = new ArrayList<TestData>();
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(TEST_FILE));
            String str;
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[list.size()];
        int count = 0;
        for (String s : list) {
            array[count++] = s;
        }

        int i = 0;
        while (i < array.length) {
            if (array[i].startsWith("connect:")) {
                version = array[i].replaceAll("connect:", "").trim();
            }
            if (array[i].startsWith("Class:")) {
                TestData testData = new TestData();
                testData.setClassName(array[i].replaceAll("Class:", "").trim());
                ++i;

                while (array[i].trim().startsWith("Command:")) {
                    String command = array[i++].replaceAll("Command:", "").trim();
                    testData.setCommand(command);
                }
                while (array[i].trim().startsWith("Param:")) {
                    String param = array[i++].replaceAll("Param:", "").trim();
                    testData.getParams().add(param.equals("null") ? null : param);
                }
                while (array[i].trim().startsWith("ResponseException:")) {
                    String message = array[i++].replaceAll("ResponseException:", "").trim();
                    testData.setException(message);
                    if (i > array.length - 1) {
                        break;
                    }
                }
                if (i > array.length - 1) {
                    break;
                }
                while (array[i].trim().startsWith("Response:")) {
                    String response = array[i++].replaceAll("Response:", "").trim();
                    testData.getResult().add(response.equals("null") ? null : response);
                    if (i > array.length - 1) {
                        break;
                    }
                }
                testDataList.add(testData);
            } else {
                ++i;
            }
        }
    }

    /**
     * This may look inefficient but it's not as the first in the list should always
     * get hit :)
     *
     * @param command
     * @return
     */
    private Collection<String> lookupResponse(MPDCommand command) throws MPDResponseException {
        String className = MockUtils.getClassName();

        for (TestData data : testDataList) {
            if (className.equals(data.getClassName())) {
                if (data.getCommand().equals(command.getCommand().trim())) {
                    if (data.getParams().containsAll(command.getParams())) {
                        testDataList.remove(data);
                        if (data.getException() != null) {
                            throw new MPDResponseException(data.getException(), data.getCommand());
                        }
                        return data.getResult();
                    }
                }
            }
        }
        return null;
    }

    public String getVersion() {
        return version;
    }

    private class TestData {
        private String command;
        private List<String> params;
        private List<String> result;
        private String exception;
        private String className;

        public TestData() {
            params = new ArrayList<String>();
            result = new ArrayList<String>();
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
