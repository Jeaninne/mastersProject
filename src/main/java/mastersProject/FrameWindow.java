package mastersProject;

import mastersProject.ImagePHash;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static mastersProject.CreateHTML.openResult;

class FrameWindow extends Frame implements ActionListener, WindowListener, ComponentListener {
	MenuBar mb;
	Menu mFile;
	MenuItem miOpen;
	MenuItem miCreateMap;
	MenuItem miExit;
	String szCurrentFilename = "";
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JSlider slider;
	ScrollPane sp;
	private Panel controlPanel;
    private Image scaledImage;
	private HashMapOfFiles curDir;
	Image image;
	imgViewer iv;
	MediaTracker mt;
	private int sensitivity = 20;

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

	private ResultMap createListOfSimilarImages() throws Exception {
		ResultMap massive = new ResultMap(szCurrentFilename);
		String imgHash = ParseDirectory.getPHashByDirectory(szCurrentFilename);
		for (String hash : curDir.currentDirectory.keySet()) {
			massive.resultForImage.put(ImagePHash.distance(curDir.currentDirectory.get(hash), imgHash), hash);
		}
		return massive;
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
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			curDir = ParseDirectory.parseDirectoryForImage(chooser.getSelectedFile().toString());
	      }
	    else {
			JOptionPane.showMessageDialog(null, "No Selection ", "Smth went wrong ", JOptionPane.INFORMATION_MESSAGE);
	      }	
	}
	
	
	void FileOpen() throws Exception {
		FileDialog fdlg;
		fdlg = new FileDialog(this, "Open file", FileDialog.LOAD);
		fdlg.setFile("*.gif;*.jpg");
		fdlg.show();

		szCurrentFilename = fdlg.getDirectory() + fdlg.getFile();


		image = Toolkit.getDefaultToolkit().getImage(szCurrentFilename);

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
		sp.setBounds(15, 55, 500, 500);

		showLabel("Full pass to file: "+szCurrentFilename, 560, 100);
		showLabel("Set sencetive for PHash algorithm", 560, 130);
		slider = new JSlider();
		slider.setMinorTickSpacing(4);
		//slider.setPaintTicks(true);
		slider.setValue(80);
		slider.setLocation(560,160);

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				sensitivity = (slider.getValue() / 4);
			}
		});
		add(slider, BorderLayout.SOUTH);
		slider.setSize(200,25);
		slider.setVisible(true);
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

	void showInformation() {
		Button searchForImageButton = new Button("Search in uploaded directory by PHash");
		searchForImageButton.setLocation(15, 560);
		searchForImageButton.setSize(230, 26);
		searchForImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap<Integer, String> result = createListOfSimilarImages().resultForImage;
					ArrayList<String> mostSimilarImages = new ArrayList<String>();
					for (int i : result.keySet()) {
						if (i<sensitivity) {
							mostSimilarImages.add(result.get(i));
							System.out.println(i);
						}
					}
					openResult(mostSimilarImages);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		add(searchForImageButton);
	}

	void showLabel(String text, int x, int y) {
		Label nl = new Label(text);
		nl.setLocation(x, y);
		nl.setSize(400, 30);
		add(nl);
		showInformation();
	}
}
