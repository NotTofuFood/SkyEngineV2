package texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
	public static List<Integer> height_values = new ArrayList<>();

	private static String last_loaded = "";
	
	public static BufferedImage loadImage(String filename) {
		BufferedImage image_loader = null;
		try {
			image_loader = ImageIO.read(new File(filename));
		} catch (IOException e) {
			BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
			Graphics g = (Graphics) missing_texture.getGraphics();
			g.setColor(Color.PINK);
			g.fillRect(0, 0, 16, 16);
			g.setColor(Color.BLACK);
			g.fillRect(16, 0, 16, 16);
			g.setColor(Color.PINK);
			g.fillRect(0, 16, 16, 16);
			g.setColor(Color.BLACK);
			g.fillRect(16, 16, 16, 16);
			g.dispose();
			image_loader = missing_texture;
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
				BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
				Graphics g = (Graphics) missing_texture.getGraphics();
				g.setColor(Color.PINK);
				g.fillRect(0, 0, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 0, 16, 16);
				g.setColor(Color.PINK);
				g.fillRect(0, 16, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 16, 16, 16);
				g.dispose();
				image_loader = missing_texture;
				e.printStackTrace();
			}
			createWallTextures((int)Display.manager.wall_width, image_loader);
			createWallHeightsTextures((int)Display.manager.wall_width, wall, image_loader);
			wall.setHas_height(false);
		}
		return image_loader;
	}
	
	public static BufferedImage loadImage(String filename, String height_filename, Wall wall) {
		BufferedImage image_loader = null;
		BufferedImage height_image_loader = null;
		if(last_loaded != filename) {
			try {
				image_loader = ImageIO.read(new File(filename));
				height_image_loader = ImageIO.read(new File(height_filename));
			} catch (IOException e) {
				BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
				Graphics g = (Graphics) missing_texture.getGraphics();
				g.setColor(Color.PINK);
				g.fillRect(0, 0, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 0, 16, 16);
				g.setColor(Color.PINK);
				g.fillRect(0, 16, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 16, 16, 16);
				g.dispose();
				image_loader = missing_texture;
				height_image_loader = missing_texture;
				e.printStackTrace();
			}
			createWallTextures((int)Display.manager.wall_width, image_loader);
			createWallHeightsTextures((int)Display.manager.wall_width, wall, height_image_loader);
		}
		return image_loader;
	}
	
	
	private static BufferedImage resizeTexture(BufferedImage texture, int width, int height) {
		Image tex = texture.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage scaled_texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		scaled_texture.getGraphics().drawImage(tex,0,0,null);
		return scaled_texture;
	}
	
	public static BufferedImage loadTileset(String filename, int row, int col, int tex_size) {
		BufferedImage image_loader = null;
		if(last_loaded != filename) {
			try {
				image_loader = ImageIO.read(new File(filename));
				switch(tex_size) {
				case 8:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*8, image_loader.getHeight()*8);
					break;
				case 16:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*4, image_loader.getHeight()*4);
					break;
				case 32:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*2, image_loader.getHeight()*2);
					break;
				default: 
					break;
				}
			} catch (IOException e) {
				BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
				Graphics g = (Graphics) missing_texture.getGraphics();
				g.setColor(Color.PINK);
				g.fillRect(0, 0, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 0, 16, 16);
				g.setColor(Color.PINK);
				g.fillRect(0, 16, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 16, 16, 16);
				g.dispose();
				image_loader = missing_texture;
				e.printStackTrace();
			}
				createWallTextures((int)Display.manager.wall_width, image_loader.getSubimage(row, col, 64, 64));
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
				BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
				Graphics g = (Graphics) missing_texture.getGraphics();
				g.setColor(Color.PINK);
				g.fillRect(0, 0, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 0, 16, 16);
				g.setColor(Color.PINK);
				g.fillRect(0, 16, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 16, 16, 16);
				g.dispose();
				final_image = missing_texture;
				e.printStackTrace();
			}
		}
		return final_image;
	}
	
	public static BufferedImage loadFloorTileset(String filename, int row, int col, int tex_size) {
		BufferedImage image_loader = null;
		BufferedImage final_image = null;
		if(last_loaded != filename) {
			try {
				image_loader = ImageIO.read(new File(filename)).getSubimage(row, col, tex_size, tex_size);
				switch(tex_size) {
				case 8:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*8, image_loader.getHeight()*8);
					break;
				case 16:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*4, image_loader.getHeight()*4);
					break;
				case 32:
					image_loader = resizeTexture(image_loader, image_loader.getWidth()*2, image_loader.getHeight()*2);
					break;
				default: 
					break;
				}
			    final_image = new BufferedImage(image_loader.getWidth(), image_loader.getHeight(), BufferedImage.TYPE_INT_RGB);
			    Graphics g = final_image.getGraphics();
			    g.drawImage(image_loader, 0, 0, null);
			    g.dispose();
			} catch (IOException e) {
				BufferedImage missing_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
				Graphics g = (Graphics) missing_texture.getGraphics();
				g.setColor(Color.PINK);
				g.fillRect(0, 0, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 0, 16, 16);
				g.setColor(Color.PINK);
				g.fillRect(0, 16, 16, 16);
				g.setColor(Color.BLACK);
				g.fillRect(16, 16, 16, 16);
				g.dispose();
				final_image = missing_texture;
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
	
	private static void createWallHeightsTextures(int wall_width, Wall wall, BufferedImage texture) {
		for(int ray = 0; ray < Window.WIDTH; ray++) {
			int wall_offset = (int)ExtraMath.clamp(ray%wall_width, 0, texture.getWidth()-1);
			int height = (texture.getSubimage(wall_offset, 0, 1, texture.getHeight()).getRGB(0, texture.getHeight()/2)>>16)&0xFF;
			height_values.add(height);
			wall.setHas_height(true);
		}
	}
	
}
