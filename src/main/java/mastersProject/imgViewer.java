package mastersProject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

class imgViewer extends Panel {
	Image im;
	Dimension dimMinSize;


	public imgViewer(Image img, Dimension dim) {
		im = img;
		dimMinSize = dim;
	}

	public void paint(Graphics g) {
		g.drawImage(im, 0, 0, this);
	}  

	public Dimension getPreferredSize() {
		return dimMinSize;
	}

	public Dimension getMinimumSize() {
		return dimMinSize;
	}
	
	public Dimension preferredSize() {
		return dimMinSize;
	}

	public Dimension minimumSize() {
		return dimMinSize;
	}
}