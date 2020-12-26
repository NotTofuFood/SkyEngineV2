package scene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import main.Window;
import texture.ImageLoader;

public class Skybox {
	
	private int x;
	private int y;
	
	private BufferedImage sprite;
	
	public Skybox(int x, int y, String filename) {
		this.x = x;
		this.y = y;
		sprite = ImageLoader.loadImage(filename);
	}
	
	public void render(ImageObserver io, Graphics2D g) {
		g.drawImage(sprite, x-(int)Window.WIDTH, y, (int)Window.WIDTH, (int)Window.HEIGHT, io);
		
		g.drawImage(sprite, x, y, (int)Window.WIDTH, (int)Window.HEIGHT, io);
		
		g.drawImage(sprite, x+(int)Window.WIDTH, y, (int)Window.WIDTH, (int)Window.HEIGHT, io);
	}
	
	public void changeSprite(String filename) {
		sprite = ImageLoader.loadImage(filename);
	}
	
	public void move(float speed) {
		if(x < -Window.WIDTH) {
			x = 0;
		} else if(x > Window.WIDTH) {
			x = 0;
		}
		x+=speed;
	}
	
}
