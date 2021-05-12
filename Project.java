import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

//A simple photo viewer. Allows the opening of pictures from the file system, seeing and opening recently opened pictures and resizing the photos to match changes in window size.
public class Project extends JFrame{
	JMenuBar menuBar;
	JMenu fileMenu, openRecentSelect;
	JMenuItem openFileSelect, resize;
	JLabel pic;
	JScrollPane scroll;
	JFrame frame;
	ImageIcon icon;
	ArrayList<File> recentFiles=new ArrayList<File>();
	int[] picSize= new int[2];
	
	public Project() {
		frame= new JFrame();
		frame.setTitle("Photo Opener");
		frame.setPreferredSize(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
		
        openFileSelect=makeMenuItem("Open File",(e)->{openAndSelectFile(pic);updateRecent(pic, openRecentSelect);});
        openRecentSelect=new JMenu("Recent");
        fileMenu = new JMenu("File");
        resize=makeMenuItem("Resize",(e)->{resizePicture();});
        fileMenu.add(openFileSelect);
        fileMenu.addSeparator();
        fileMenu.add(resize);
        fileMenu.addSeparator();
        fileMenu.add(openRecentSelect);
        menuBar=new JMenuBar();
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        pic=new JLabel();
        icon = new ImageIcon("IMG_9300.PNG");
        pic=new JLabel(icon);
        
        scroll=new JScrollPane(pic);
        frame.add(scroll);
        frame.pack();
        picSize[0]=pic.getSize().width;
		picSize[1]=pic.getSize().height;
        
	}
	public Dimension getScaledDimension( Dimension boundary) {
		//window dimensions before resizing
	    int original_width = picSize[0];
	    int original_height = picSize[1];
	    //current windows dimensions
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    //final window dimensions
	    int new_width = original_width;
	    int new_height = original_height;
	    //Screen resolution
	    Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
		double screen_width = screenRes.getWidth();
		double screen_height = screenRes.getHeight();
	    System.out.println("originaler:"+original_width+" "+original_height);
	    //prevents window from being larger than the screen
	    if(bound_width>screen_width)
	    	bound_width=(int) screen_width;
	    if(bound_height>screen_height)
	    	bound_width=(int) screen_height;
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }
	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
	    if (original_width < bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        System.out.println(new_width);
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	        System.out.println(new_height);
	    }
	    // then check if we need to scale even with the new height
	    if (new_height < bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
	    System.out.println("new width:"+bound_width+" "+bound_height);
	    
	    return new Dimension(new_width, new_height);
	}
	
	//Function to resize the picture to current window size
	public void resizePicture() {
		Dimension windowSize = frame.getContentPane().getSize();
		Dimension d=getScaledDimension(windowSize);
		pic.setBounds(0,0,d.width,d.height);
		Image img = icon.getImage();
		Image newImg=img.getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon newImc=new ImageIcon(newImg);
		pic.setIcon(newImc);
		frame.pack();
		picSize[0]=pic.getSize().width;
		picSize[1]=pic.getSize().height;
	}
	
	//Loads the picture
	public void openFile(File fileToOpen, JLabel picture) {
		icon = new ImageIcon(fileToOpen.getAbsolutePath());
		pic.setIcon(icon);
		addToRecent(fileToOpen);
		frame.pack();
		picSize[0]=pic.getSize().width;
		picSize[1]=pic.getSize().height;
//		resizer();
	}
	
	//Takes in two strings and returns a menu item using the first string 
    //as the text and the second as the action command
    public JMenuItem makeMenuItem(String name, ActionListener al) {
    	JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(al);
        return menuItem;
    }
    //Opens a file in the note pad
    public void openAndSelectFile(JLabel picture) {
    	File fileToOpen = null;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            fileToOpen = fc.getSelectedFile();
        openFile(fileToOpen,picture);
    }
    //This method updates the menu displaying recently opened files
    public void updateRecent(JLabel picture,JMenu openRecentSelect) {
    	openRecentSelect.removeAll();
    	int x=0;
    	for(File file:recentFiles) {
    		final int ItemIndex=x;
        	JMenuItem recents=makeMenuItem(file.getName(),(e)->{openRecent(ItemIndex,picture);});
        	x++;
        	openRecentSelect.add(recents);
        }
    }
  //Takes in an integer which represents which file in the recently opened array to open, and opens that file
    public void openRecent(int x,JLabel picture) {
    	File fileToOpen=recentFiles.get(x);
    	openFile(fileToOpen,picture);
    }
  //Takes in a file and adds it to the recently opened files array. Drops the oldest file 
    //if array has 5 or more items in it.
    public void addToRecent(File file) {
    	if(!recentFiles.contains(file)) {
    		if(recentFiles.size()>=5) {
    			recentFiles.remove(0);
    		}
    		recentFiles.add(file);
    	}
    }
	
public static void main(String[] args) {
		Project p=new Project();
	}
}
