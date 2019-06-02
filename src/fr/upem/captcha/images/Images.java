package fr.upem.captcha.images;

import java.net.URL;

import java.util.List;

/**
 * This interface implements the main methods of an Image.
 * @author Guillaume Lollier & Michel Yip
 */
public interface Images {
	/**
	 * Get the list of URL from the images of the current category
	 * @return the list of URL from the images of the current category
	 */
	public List<URL> getPhotos();
	
	public List<URL> getRandomPhotosURL(int value);
	
	public List<URL> getRandomPhotoURL();
	
	/**
	 * Return true if the url parameter is in the current category, false otherwise
	 * @return true if the url parameter is in the current category, false otherwise
	 */
	public boolean isPhotoCorrect(URL url);
	
}
