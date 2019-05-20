package fr.upem.captcha.captchamanager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.images.animaux.Animaux;
import fr.upem.captcha.images.animaux.chat.Chat;
import fr.upem.captcha.images.animaux.poulet.Poulet;
import fr.upem.captcha.images.animaux.singe.Singe;
import fr.upem.captcha.images.boissons.Boissons;
import fr.upem.captcha.images.boissons.alcool.Alcool;
import fr.upem.captcha.images.boissons.jus.Jus;
import fr.upem.captcha.images.boissons.sodas.Sodas;
import fr.upem.captcha.images.legumes.Legumes;
import fr.upem.captcha.images.legumes.concombre.Concombre;
import fr.upem.captcha.images.legumes.endive.Endive;
import fr.upem.captcha.images.legumes.tomate.Tomate;

/**
 * This class is used to manage to logic for the application. 
 * It is a singleton, which means it is only instanciated once per execution.
 * 
 * @author Guillaume Lollier & Michel Yip
 */
public class CaptchaManager {
	private final static CaptchaManager instance = new CaptchaManager();
	
	private final static int MAX_DIFFICULTY = 4;
	private final static int MIN_NUMBER_OF_CORRECT_IMAGES = 3;
	private final static int MAX_NUMBER_OF_CORRECT_IMAGES = 6;
	private final static int GRID_SIZE = 9;
		
	private ArrayList<Category> categories;
	private ArrayList<Category> correctCategories;
	private int difficulty;
	private ArrayList<URL> captchaImages;
	private ArrayList<URL> correctImages;
	private int correctImagesNumber;
	
	/**
	 * Create the instance of captcha manager, it is a singleton and can only be instanciated once.
	 * @return a CaptchaManager singleton.
	 */
	private CaptchaManager() {
		difficulty = 1;
		categories = new ArrayList<Category>();
		getCategories();
		correctCategories = new ArrayList<Category>();
		setCorrectCategories(difficulty);
		captchaImages = new ArrayList<URL>();
		correctImages = new ArrayList<URL>();
		setCaptchaImages();	
	}
	
	/**
	 * Set the list of possible image categories to select images from based on the current difficulty.
	 */
	public void getCategories() {
		if (difficulty <= 2) {
			categories.add(new Animaux());
			categories.add(new Boissons());
			categories.add(new Legumes());
		} else {
			categories.add(new Chat());
			categories.add(new Poulet());
			categories.add(new Singe());
			categories.add(new Alcool());
			categories.add(new Jus());
			categories.add(new Sodas());
			categories.add(new Endive());
			categories.add(new Tomate());
			categories.add(new Concombre());
		}
	}
	
	/**
	 * Set the list of possible categories to validate the captcha based on the difficulty parameter.
	 * @param difficulty.
	 */
	public void setCorrectCategories(int difficulty) {
		Collections.shuffle(categories);
		for (int i = 0; i < difficulty; i++) {
			correctCategories.add(categories.get(i));
		}
	}
	
	/**
	 * Return the list of correct categories.
	 * @return the list of correct categories.
	 */
	public ArrayList<Category> getCorrectCategories(){
		return correctCategories;
	}
	
	/**
	 * Return the current difficulty level.
	 * @return the current difficulty.
	 */
	public int getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Return the number of possible difficulty.
	 * @return the max difficulty level.
	 */
	public int getMaxDifficulty() {
		return MAX_DIFFICULTY;
	}
	
	/**
	 * Increment the current difficulty level by one.
	 */
	public void increaseDifficulty() {
		difficulty++;
	}
	
	/**
	 * Clear the list of possible categories and correct categories.
	 */
	public void clearCategories() {
		categories.clear();
		correctCategories.clear();
	}
	
	/**
	 * Set the number of correct images in the captcha with the number parameter.
	 * @param number
	 */
	public void setCorrectImagesNumber(int number) {
		correctImagesNumber = number;
	}
	
	/**
	 * Return the number of correct images in the captcha.
	 * @return the number of correct images in the captcha.
	 */
	public int getCorrectImageNumber() {
		return correctImagesNumber;
	}	
	
	/**
	 * Set the images that will be displayed in the captcha.
	 */
	public void setCaptchaImages() {
		captchaImages.clear();
		correctImages.clear();
		int tmp =  MIN_NUMBER_OF_CORRECT_IMAGES + (int)(Math.random() * ((MAX_NUMBER_OF_CORRECT_IMAGES - MIN_NUMBER_OF_CORRECT_IMAGES) + 1));
		setCorrectImagesNumber(tmp);
		
		ArrayList<URL> correctImagesURL = new ArrayList<URL>();
		for (Category c : correctCategories) {
			correctImagesURL.addAll(c.getPhotos());
		}
		Collections.shuffle(correctImagesURL);
		for (int i = 0; i < correctImagesNumber; i++) {
			correctImages.add(correctImagesURL.get(i));
			captchaImages.add(correctImagesURL.get(i));
		}
		
		ArrayList<URL> incorrectImagesURL = new ArrayList<URL>();
		ArrayList<Category> incorrectCategories = new ArrayList<Category>(categories);	
		incorrectCategories.removeAll(correctCategories);
		for (Category c : incorrectCategories) {
			incorrectImagesURL.addAll(c.getPhotos());
		}
		Collections.shuffle(incorrectImagesURL);
		for (int i = 0; i < (GRID_SIZE - correctImagesNumber); i++) captchaImages.add(incorrectImagesURL.get(i));
		
		Collections.shuffle(captchaImages);	
		
	}

	/**
	 * Return the images from the captcha.
	 * @return the images from the captcha as an ArrayList of URL.
	 */
	public ArrayList<URL> getCaptchaImages(){
		return captchaImages;
	}
	
	/**
	 * Return the instance of the captcha manager.
	 * @return the instance of CaptchaManager.
	 */
	public static final CaptchaManager getInstance() {
		return instance;
	}
	
	/**
	 * Check if the selectedImages parameter correspond to the array of correct images.
	 * Return true if it correspond, return false otherwise.
	 * @param selectedImages.
	 * @return true if selectedImages correspond to the correct images, otherwise return false.
	 */
	public boolean captchaIsCorrect(ArrayList<URL> selectedImages) {
		if(getCorrectImageNumber() != selectedImages.size()) {
			return false;
		}
		for (URL image : selectedImages) {
		    boolean inCat = false;
		    for (Category cat : getCorrectCategories()) {
		        if (cat.isPhotoCorrect(image)) {
		            inCat = true;
		        }
		    }
		    if (inCat == false) {
		      return false;
		    }
		}
		return true;
		
	}
	
}
