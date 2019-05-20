package fr.upem.captcha.images;

import java.io.IOException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class Category implements Images {
	private List<URL> photos;
	
	public Category() {
		super();
		photos = new ArrayList<URL>();
		getPhotos();
	}
	
	
	@Override
	public List<URL> getPhotos() {
		photos.clear();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String currentPath = packageName.replace('.', '/');
		Path currentRelativePath = Paths.get(currentPath);
		
		// Getting all sub directories
		List<String> directories = null;

		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0); // Remove the current directory
		} catch (IOException e) {
			e.printStackTrace();
		}

		// For each categogy, we get the sub-directory
		for (String directory : directories) {
			Path childPath = Paths.get(currentPath + "/" + directory);
			// Getting images
			List<String> images = null;
			try {
				images = Files.walk(childPath, 2)
				        .map(Path::getFileName)
				        .map(Path::toString)
				        .filter(n -> n.contains(".jpg"))
				        .collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(directory + "/" + image));
			}
		}

		// S'il n'y a pas de sous dossier, on r�cup�re les images directement dans le dossier actuel
		if (directories.isEmpty()) {
			List<String> images = null;
			try {
				images = Files.walk(currentRelativePath, 1)
				        .map(Path::getFileName)
				        .map(Path::toString)
				        .filter(n -> n.contains(".jpg"))
				        .collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(image));
			}
		}
		
		return photos;
	}	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public boolean isPhotoCorrect(URL url) {
		String currentCategory = this.getClass().getPackage().getName().replace(".", "/");
		//System.out.println("current category " + currentCategory);
		return url.toString().contains(currentCategory);
	}
}