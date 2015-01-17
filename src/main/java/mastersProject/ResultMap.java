package mastersProject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResultMap {
    String image;
    HashMap<Integer, String> resultForImage;

    public ResultMap(String imageDir) {
        this.image = imageDir;
        resultForImage = new HashMap<Integer, String>();
    }
}
