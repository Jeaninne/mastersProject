package mastersProject;

import java.util.HashMap;

public class HashMapOfFiles {

    String directory;
    HashMap<String, String> currentDirectory = new HashMap<String, String>();

    public HashMapOfFiles(String directory) {
        this.directory = directory;
    }

    public void addToHashMap(String fileDir) {
        try {
            currentDirectory.put(fileDir, ParseDirectory.getPHashByDirectory(fileDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
