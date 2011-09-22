package me.tedyoung.blog;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {
	@Value("${image.path}")
	private File baseDirectory;
	
	public ImageRepository() {
	}
	
	private File getFile(Object identifier) {
		return new File(baseDirectory, identifier + ".jpg");
	}
	
	public InputStream loadImage(Object identifier) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(getFile(identifier)));
	}
	
	public BufferedImage loadBufferedImage(Object identifier) throws FileNotFoundException, IOException {
		return ImageIO.read(loadImage(identifier));
	}
	
	public void saveImage(Object identifier, InputStream source) throws IOException {
		BufferedImage image = ImageIO.read(source);
		
		BufferedImage scaled = new BufferedImage(300, 200, image.getType());
		Graphics2D g = (Graphics2D) scaled.getGraphics();
		g.drawImage(image, 0, 0, 300, 200, null);
		g.dispose();
		
		ImageIO.write(scaled, "JPG", new BufferedOutputStream(new FileOutputStream(getFile(identifier))));
	}
 
	public void deleteImage(Object identifier) {
		getFile(identifier).delete();
	}
	
	public boolean hasImage(Object identifier) {
		return getFile(identifier).exists();
	}
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

}
