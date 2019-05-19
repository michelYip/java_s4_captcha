package fr.upem.captcha.images;

import java.net.URL;

import java.util.List;
import java.util.ArrayList;


public interface Images {
	public List<URL> getPhotos();
	
	public List<URL> getRandomPhotosURL(int value);
	
	public List<URL> getRandomPhotoURL();
	
	public boolean isPhotoCorrect(URL url);
	
}
