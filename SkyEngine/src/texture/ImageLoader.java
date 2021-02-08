package texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main.Window;
import maths.ExtraMath;
import obj.Wall;
import renderer.Display;

public class ImageLoader {
	
	public static List<BufferedImage> wall_textures = new ArrayList<>();
	
	public static BufferedImage[][] floor_textures = new BufferedImage[64*64][64*64];

	private static String last_loaded = "";
	
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
	
	public static BufferedImage loadImage(String filename, Wall wall) {
		BufferedImage image_loader = null;
		if(last_loaded != filename) {
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
			createWallTextures((int)Display.manager.wall_width, image_loader);
			wall.textureID++;
		}
		return image_loader;
	}
	
	public static BufferedImage loadFloorImage(String filename) {
		BufferedImage image_loader = null;
		BufferedImage final_image = null;
		if(last_loaded != filename) {
			try {
				image_loader = ImageIO.read(new File(filename));
			    final_image = new BufferedImage(image_loader.getWidth(), image_loader.getHeight(), BufferedImage.TYPE_INT_RGB);
			    Graphics g = final_image.getGraphics();
			    g.drawImage(image_loader, 0, 0, null);
			    g.dispose();
			} catch (IOException e) {
				try {
					final_image = ImageIO.read(new File("res/textures/important/missing_texture.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return final_image;
	}
	
	private static void createWallTextures(int wall_width, BufferedImage texture) {
		for(int ray = 0; ray < Window.WIDTH; ray++) {
			int wall_offset = (int)ExtraMath.clamp(ray%wall_width, 0, texture.getWidth()-1);
			wall_textures.add(texture.getSubimage(wall_offset, 0, 1, texture.getHeight()));
		}
	}
	
}
