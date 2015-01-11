package mastersProject;

import java.awt.Toolkit;

public class ImageForSearch {
	Integer height;
	Integer width;
	
	ImageForSearch(String szCurrentFilename) {
		Toolkit.getDefaultToolkit().getImage(szCurrentFilename);  
	}

}
