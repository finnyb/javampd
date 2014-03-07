package mockeddata;

import org.bff.javampd.exception.MPDException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataLoader {
    private static List<File> testRootFiles;

    private static final String PATH = "./src/it/resources/TestMp3s";

    public static Collection<File> getTestFiles(File directory) {
        List<File> testFiles = new ArrayList<File>();
        File[] files = directory.listFiles();
        for (File file : files) {
            testFiles.add(file);
        }

        return testFiles;
    }

    public static void loadData(File f) throws MPDException {
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadData(file);
            } else {
                if (file.getName().endsWith(Songs.EXTENSION)) {
                    Songs.loadSong(file, f);
                }
            }
        }
    }

    public static Collection<File> getRootTestFiles() {
        if (testRootFiles == null) {
            testRootFiles = new ArrayList<>();
            File[] files = new File(PATH).listFiles();
            for (File file : files) {
                if (!file.getName().startsWith("ReadMe")
                        && !file.getName().startsWith("TestWaveFile")) {
                    testRootFiles.add(file);
                }
            }
        }
        return testRootFiles;
    }

    public static void loadData() {
        try {
            loadData(new File(PATH));
        } catch (MPDException e) {
            e.printStackTrace();
        }
    }
}
