package fr.upem.captcha.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.captchamanager.CaptchaManager;
import fr.upem.captcha.ui.MainUI;

/**
 * This class generate the main interface of the application. 
 * It listens to event between the user and the interface.
 * @author Guillaume Lollier & Michel Yip
 */
public class MainUI {	
	private final static int width = 800;
	private final static int height = 800;	
	private static JFrame frame = new JFrame("Captcha Guillaume LOLLIER & Michel YIP");
	private static CaptchaManager captchaManager = CaptchaManager.getInstance();

	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException  {
		GridLayout layout = createLayout();
		
		frame.setLayout(layout);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createApplicationWindow();
	}
	
	/**
	 * Return a grid layout
	 * @return a grid layout
	 */
	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}
	
	/**
	 * Create the main window of the application
	 */
	public static void createApplicationWindow() {
		frame.getContentPane().add(new JTextArea("Are you a bot ?"));
		frame.getContentPane().add(new JButton(new AbstractAction("Yes") {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						System.exit(0);
					}
				});
			}
		}));
		frame.getContentPane().add(new JButton(new AbstractAction("No") {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						frame.getContentPane().removeAll();
						drawGrid("");
					}
				});
			}
		}));
		frame.setVisible(true);
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	/**
	 * Create the closing window of the application with a feedback
	 * @param feedback message to show to user
	 */
	public static void createApplicationClosingWindow(String feedback) {
		frame.getContentPane().add(new JTextArea(feedback));
		frame.getContentPane().add(new JButton(new AbstractAction("Leave the Captcha") {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						System.exit(0);
					}
				});
			}
		}));
		frame.setVisible(true);
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	/**
	 * Draw the grid of the captcha based on the instance of captcha manager.
	 * Show any feedback to the user
	 * @param feedback message to show the user
	 */
	public static void drawGrid(String feedback) {
		try {	
			for (URL image : captchaManager.getCaptchaImages()) {
				frame.add(createLabelImage(image));
			}
			frame.getContentPane().add(createInstructionText());
			frame.getContentPane().add(createOkButton());
			frame.getContentPane().add(createFeedbackText(feedback));
			frame.setVisible(true);
			frame.getContentPane().validate();
			frame.getContentPane().repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the instruction textbox for the user
	 * @return the instruction textbox 
	 */
	public static JTextArea createInstructionText() {
		return new JTextArea("Difficulty : "+ captchaManager.getDifficulty() 
				+"\nClick on the images with the following categories :\n" 
				+ captchaManager.getCorrectCategories());
	}
	
	/**
	 * Create the button used to validate the captcha
	 * @return the button used to validate the captcha
	 */
	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Validate captcha") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						CaptchaManager captchaManager = CaptchaManager.getInstance();
						if(captchaManager.captchaIsCorrect(selectedImages)) {
							frame.getContentPane().removeAll();
							createApplicationClosingWindow("The captcha is correct !");
						} else {
							if (captchaManager.getDifficulty() >= captchaManager.getMaxDifficulty()) {
								frame.getContentPane().removeAll();
								createApplicationClosingWindow("Permission denied !");
							} else {
								captchaManager.clearCategories();
								captchaManager.increaseDifficulty();
								captchaManager.getCategories();
								captchaManager.setCorrectCategories(captchaManager.getDifficulty());
								captchaManager.setCaptchaImages();
								frame.getContentPane().removeAll();
								drawGrid("You're a robot or you made mistakes.\nIncreasing difficulty... Try again");
								selectedImages.clear();
							}
							
						}
					}
				});
			}
		});
	}
	
	/**
	 * Create the feedback message textbox to show the user
	 * @param feedback
	 * @return the feedback message textbox
	 */
	public static JTextArea createFeedbackText(String feedback) {
		return new JTextArea(feedback);
	}
	
	/**
	 * Create an image from the imageLocation parameter
	 * @param imageLocation
	 * @return the image as a JLabel
	 * @throws IOException
	 */
	private static JLabel createLabelImage(URL imageLocation) throws IOException{
		final URL url = imageLocation;
		
		BufferedImage img = ImageIO.read(url);
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH);
		
		final JLabel label = new JLabel(new ImageIcon(sImage));
		
		label.addMouseListener(new MouseListener() {
			private boolean isSelected = false;
			
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}
						
					}
				});
				
			}
		});
		
		return label;
	}
}
