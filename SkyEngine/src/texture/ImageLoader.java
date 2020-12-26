package texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(String filename) {
		BufferedImage image_loader = null;
		try {
			image_loader = ImageIO.read(new File(filename));
		} catch (IOException e) {
			try {
				image_loader = ImageIO.read(new File("res/textures/important/missing_texture.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return image_loader;
	}
	
}
