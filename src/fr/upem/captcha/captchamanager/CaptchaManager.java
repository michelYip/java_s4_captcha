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
import fr.upem.captcha.images.legumes.jaune.Jaune;
import fr.upem.captcha.images.legumes.rouge.Rouge;
import fr.upem.captcha.images.legumes.vert.Vert;

public class CaptchaManager {
	private final static int MAX_DIFFICULTY = 4;
	private final static int MIN_NUMBER_OF_CORRECT_IMAGES = 3;
	private final static int MAX_NUMBER_OF_CORRECT_IMAGES = 6;
	private final static int GRID_SIZE = 9;
	
	private final static CaptchaManager instance = new CaptchaManager();
	
	private ArrayList<Category> categories;
	private ArrayList<Category> correctCategories;
	private int difficulty;
	private ArrayList<URL> captchaImages;
	private ArrayList<URL> correctImages;
	
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
	
	public void getCategories() {
		switch(difficulty) {
			case 1:
				categories.add(new Animaux());
				categories.add(new Boissons());
				categories.add(new Legumes());
				break;
			case 3:
				categories.add(new Chat());
				categories.add(new Poulet());
				categories.add(new Singe());
				categories.add(new Alcool());
				categories.add(new Jus());
				categories.add(new Sodas());
				categories.add(new Jaune());
				categories.add(new Rouge());
				categories.add(new Vert());
				break;
			default:
				break;
		}
	}
	
	public void setCorrectCategories(int size) {
		System.out.println("setCorrectCategories");
		Collections.shuffle(categories);
		for (int i = 0; i < difficulty; i++) {
			correctCategories.add(categories.get(i));
		}
	}
	
	public void increaseDifficulty() {
		difficulty++;
	}
	
	public void clearCategories() {
		categories.clear();
		correctCategories.clear();
	}
	
	public void setCaptchaImages() {
		System.out.println("setCaptchaImages");
		captchaImages.clear();
		int correctImagesNumber = MIN_NUMBER_OF_CORRECT_IMAGES + (int)(Math.random() * ((MAX_NUMBER_OF_CORRECT_IMAGES - MIN_NUMBER_OF_CORRECT_IMAGES) + 1));
		System.out.println("correctImagesNumber = " + correctImagesNumber);
		
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
	
	public ArrayList<URL> getCaptchaImages(){
		return captchaImages;
	}
	
	public boolean validateCaptcha(ArrayList<URL> selectedImages) {
		if (selectedImages.size() != correctImages.size()) return false;
		for (URL imageURL : selectedImages) {
			//Use interface image.isPhotoCorrect
			if (!correctImages.contains(imageURL)) return false;
		}
		return true;
	}
	
	public static void main(String [] argv) {
		System.out.println("test");
	}
	
}
