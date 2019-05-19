package fr.upem.captcha.images;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;

public abstract class Category implements Images {
	private List<URL> photos;
	
	public Category() {
		super();
		photos = new ArrayList<URL>();
		getPhotos();
	}
	
	@Override
	public List<URL> getPhotos(){
		photos.clear();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String currentPath = packageName.replace('.', '/');
		
		Path currentRelativePath = Paths.get(currentPath);
		
		List<String> dir = null;
		
		//TODO : get Photos from directories
		
		return photos;
		
	}
	
}
