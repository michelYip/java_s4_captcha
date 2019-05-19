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
	public List<URL> getPhotos(){
		photos.clear();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String curPath = packageName.replace('.', '/');
		
		Path relativePath = Paths.get(curPath);

		//TODO : get Photos from directories
		List<String> dir = null;
		try {
			dir = Files.walk(relativePath, 1).map(Path::getFileName).map(Path::toString).filter(n -> !n.contains(".")).collect(Collectors.toList());
			dir.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String subDir : dir) {
			Path childPath = Paths.get(curPath + "/" + subDir);
			List <String> images = null;
			try {
				images = Files.walk(childPath, 2).map(Path::getFileName).map(Path::toString).filter(n -> n.contains(".jpg")).collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(dir + "/" + image));
			}
		}
		
		if (dir.isEmpty()) {
			List<String> images = null;
			try {
				images = Files.walk(relativePath,  1).map(Path::getFileName).map(Path::toString).filter(n -> n.contains("jpeg")).collect(Collectors.toList());
			} catch (IOException e){
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(image));
			}
		}
		
		return photos;
		
	}
	
}
