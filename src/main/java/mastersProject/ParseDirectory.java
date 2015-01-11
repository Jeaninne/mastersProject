package mastersProject;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ParseDirectory {
		
	public static HashMapOfFiles parseDirectoryForImage(String szDirectoryName) {
		HashMapOfFiles currentDirectory = new HashMapOfFiles();
		File root = new File(szDirectoryName);
		File[] files = root.listFiles();
		int i = 0;
		
		while(i<files.length) {
			File firstElement = files[i];
			File[] subFiles = null;
			 if (firstElement.isDirectory()) {
				 subFiles = firstElement.listFiles();
			 } else {
				 i++;
				 continue;
			 }
			File[] temp = new File[files.length+subFiles.length];
			for (int j=0;j<i;j++) {
				temp[j]=files[j];
			}
			for (int k=0;k<subFiles.length;k++) {
				temp[i+k+1]=subFiles[k];
			}
			for (int m=i+1;m<files.length;m++) {
				temp[m+subFiles.length]=files[m];
			}
			files = temp;
			i++;
		}
		
		
		for (int j=0;j<files.length;j++) {
			if ((files[j]!=null) &&
					(files[j].getPath().substring(files[j].getPath().length()-3).equals("jpg"))) {
				//TODO System.out.println(files[j].getPath());
				currentDirectory.addToHashMap(files[j].getPath());
			}
		}

		return currentDirectory;
	};

	public static String getPHashByDirectory(String dir) throws Exception {
		BufferedImage img = ImageIO.read(new File(dir));
		ImagePHash imagePHash = new ImagePHash();
		return imagePHash.getHash(img);
	}
}
