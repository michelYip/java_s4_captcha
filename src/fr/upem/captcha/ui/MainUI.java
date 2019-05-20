package fr.upem.captcha.ui;

import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

public class MainUI {
	private final static int TIMER = 3000;
	
	private final static int width = 800;
	private final static int height = 800;	
	private static JFrame frame = new JFrame("Captcha Guillaume LOLLIER & Michel YIP");
	private static CaptchaManager captchaManager = CaptchaManager.getInstance();

	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException  {
		System.out.println("Lancement de l'affichage");
		
		GridLayout layout = createLayout();
		
		frame.setLayout(layout);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createApplicationWindow();
	}
	
	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}
	
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
	
	public static JTextArea createInstructionText() {
		return new JTextArea("Difficulty : "+ captchaManager.getDifficulty() 
				+"\nClick on the images with the following categories :\n" 
				+ captchaManager.getCorrectCategories());
	}
	
	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") {
			
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
	
	public static JTextArea createFeedbackText(String feedback) {
		return new JTextArea(feedback);
	}
	
	private static JLabel createLabelImage(URL imageLocation) throws IOException{
		
		//final URL url = MainUi.class.getResource(imageLocation); //Aller chercher les images !! IMPORTANT 
		final URL url = imageLocation;
		
		System.out.println(url); 
		
		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // cr�er le composant pour ajouter l'image dans la fenêtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'�v�nement de souris
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
			public void mouseClicked(MouseEvent arg0) { //ce qui nous int�resse c'est lorsqu'on clique sur une image, il y a donc des choses � faire ici
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
							isSelected = true;
							selectedImages.add(url);
							//System.out.println(selectedImages);
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
