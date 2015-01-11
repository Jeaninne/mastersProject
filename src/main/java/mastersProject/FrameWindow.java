package mastersProject;

import mastersProject.ImagePHash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


class FrameWindow extends Frame implements ActionListener, WindowListener, ComponentListener {
	MenuBar mb;
	Menu mFile;
	MenuItem miOpen;
	MenuItem miCreateMap;
	MenuItem miExit;
	

	String szCurrentFilename = "";
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	ScrollPane sp;
    private Image scaledImage;
	Image image;
	imgViewer iv;
	MediaTracker mt;

	public FrameWindow(String szTitle) {
		super(szTitle);
		setSize(screenSize.width, screenSize.height-40);

		mb = new MenuBar();
		miOpen = new MenuItem("Open...");
		miCreateMap = new MenuItem("Get directory");
		miExit = new MenuItem("Exit");
		
		mFile = new Menu("File");
		mFile.add(miOpen);
		mFile.add(miCreateMap);
		mFile.add("-");
		mFile.add(miExit);
		
		mb.add(mFile);
		
		miOpen.addActionListener(this);
		miCreateMap.addActionListener(this);
		miExit.addActionListener(this);
		
		setMenuBar(mb);
		
		this.addWindowListener(this);
		this.addComponentListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(miOpen)) {
			try {
				FileOpen();
			} catch (IOException  e1) {
				JOptionPane.showMessageDialog(null, "could not upload file", "Smth went wrong ", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource().equals(miCreateMap)) {
			parseDirectory();
		} else if(e.getSource().equals(miExit)) {
			setVisible(false);
			System.exit(0);
		}
	}
	
	public void componentResized(ComponentEvent e) {
		if(sp != null) {
			doLayout();
			sp.doLayout();
			sp.repaint();
			iv.repaint();
		}  

	}

	public void componentShown(ComponentEvent e) {
		if(sp != null) {
			doLayout();
			sp.doLayout();
			sp.repaint();
			iv.repaint();
		}
	}

	public void componentMoved(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
     
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		System.exit(0);
	}

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	void parseDirectory() {
		JFileChooser chooser;
	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    // disable the "All files" option.
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			System.out.println(ParseDirectory.parseDirectoryForImage(chooser.getSelectedFile().toString()));
	      }
	    else {
	    	//TODO alert
	      System.out.println("No Selection ");
	      }	
	}
	
	
	void FileOpen() throws Exception {
		FileDialog fdlg;
		fdlg = new FileDialog(this, "Open file", FileDialog.LOAD);
		fdlg.setFile("*.gif;*.jpg");
		fdlg.show();

		szCurrentFilename = fdlg.getDirectory() + fdlg.getFile();
		
		
		image = Toolkit.getDefaultToolkit().getImage(szCurrentFilename);   
		setTitle(ParseDirectory.getPHashByDirectory(szCurrentFilename));
		
		mt = new MediaTracker(this);
		mt.addImage(image, 0);
		
		try {
			mt.waitForAll();
		} catch (InterruptedException ex) { }
		
		if(sp != null) { this.remove(sp); }  
		
		sp = new ScrollPane();
		sp.setSize(getSize());
		
		setLayout(new BorderLayout());
		add("Center", sp);
		
		Image scaledImage = getScaledImage(image, (double)500, (double)500);
		iv = new imgViewer(scaledImage, new Dimension(scaledImage.getWidth(this), scaledImage.getHeight(this)));
		
		sp.add(iv);
		sp.doLayout();
		sp.setBounds(10, 100, 500, 500);
		
	}
	
	Image getScaledImage(Image img, Double maxWidth, Double maxHeight) {
		Double ratio;
		if (img.getWidth(this)>img.getHeight(this)) {
			ratio = (maxWidth/((double)img.getWidth(this)));
		} else {
			ratio = (maxHeight/((double)img.getHeight(this)));			
		}
		
		return image.getScaledInstance(
				new Float(image.getWidth(this)*ratio).intValue(), 
				new Float(image.getHeight(this)*ratio).intValue(), 
				Image.SCALE_SMOOTH);
	}
}
