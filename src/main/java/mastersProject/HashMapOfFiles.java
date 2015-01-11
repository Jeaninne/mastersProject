package mastersProject;

import java.util.HashMap;

public class HashMapOfFiles {

    HashMap<String, String> currentDirectory = new HashMap<String, String>();

    public void addToHashMap(String fileDir) {
        try {
            currentDirectory.put(fileDir, ParseDirectory.getPHashByDirectory(fileDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
