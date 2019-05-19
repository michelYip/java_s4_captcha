package fr.upem.captcha.ui;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	
	private final static int width = 800;
	private final static int height = 800;	

	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException  {
		System.out.println("Lancement de l'affichage");
		
		CaptchaManager captchaManager = CaptchaManager.getInstance();
		
		JFrame frame = new JFrame("Captcha Michel YIP & Guillaume LOLLIER"); // Création de la fenêtre principale
		
		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(width, height); // définition de la taille
		frame.setResizable(false);  // On définit la fenÃªtre comme non redimentionnable
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.
		 
		
		JButton okButton = createOkButton();
		
		for (URL image : captchaManager.getCaptchaImages()) {
			frame.add(createLabelImage(image));
		}
		
		System.out.println(captchaManager.getCorrectCategories());
		
		frame.add(new JTextArea("Cliquez sur les images contenant \n"+ captchaManager.getCorrectCategories()));
		
		
		frame.add(okButton);
		
		frame.setVisible(true);
	}
	
	
	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}
	
	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des événements
					
					@Override
					public void run() { // c'est un runnable
						System.out.println("J'ai cliqué sur Ok");
					}
				});
			}
		});
	}
	
	private static JLabel createLabelImage(URL imageLocation) throws IOException{
		
		//final URL url = MainUi.class.getResource(imageLocation); //Aller chercher les images !! IMPORTANT 
		final URL url = imageLocation;
		
		System.out.println(url); 
		
		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenÃªtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évènement de souris
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
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
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
