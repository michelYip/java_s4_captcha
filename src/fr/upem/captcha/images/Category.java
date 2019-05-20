package fr.upem.captcha.images;

import java.io.IOException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This abstract class is a representation of a Category.
 * @author Guillaume Lollier & Michel Yip
 */
public abstract class Category implements Images {
	private List<URL> listPhotos;
	
	public Category() {
		super();
		listPhotos = new ArrayList<URL>();
		getPhotos();
	}
	
	
	@Override
	public List<URL> getPhotos() {
		listPhotos.clear();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String curPath = packageName.replace('.', '/');
		Path relativePath = Paths.get(curPath);
		
		List<String> dir = null;

		try {
			dir = Files.walk(relativePath, 1).map(Path::getFileName).map(Path::toString).filter(i -> !i.contains(".")).collect(Collectors.toList());
			dir.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String directory : dir) {
			Path childPath = Paths.get(curPath + "/" + directory);
			List<String> images = null;
			try {
				images = Files.walk(childPath, 2).map(Path::getFileName).map(Path::toString).filter(i -> i.contains(".jpg")).collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) listPhotos.add(this.getClass().getResource(directory + "/" + image));
		}

		if (dir.isEmpty()) {
			List<String> images = null;
			try {
				images = Files.walk(relativePath, 1).map(Path::getFileName).map(Path::toString).filter(i -> i.contains(".jpg")).collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) listPhotos.add(this.getClass().getResource(image));
		}
		return listPhotos;
	}	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public boolean isPhotoCorrect(URL url) {
		String currentCategory = this.getClass().getPackage().getName().replace(".", "/");
		return url.toString().contains(currentCategory);
	}
}