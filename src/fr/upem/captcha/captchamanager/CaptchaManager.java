package fr.upem.captcha.captchamanager;

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
	
	private final static CaptchaManager instance = new CaptchaManager();
	
	private ArrayList<Category> categories;
	private ArrayList<Category> correctCategories;
	private int difficulty;
	private ArrayList<Category> gridImages;
	private int numberOfCorrectImages;
	
	private CaptchaManager() {
		difficulty = 1;
		categories = new ArrayList<Category>();
		this.getCategories();
		correctCategories = new ArrayList<Category>();
		this.setCategories(difficulty);
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
	
	public void setCategories(int size) {
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
	
	public static void main(String [] argv) {
		System.out.println("test");
	}
	
}
