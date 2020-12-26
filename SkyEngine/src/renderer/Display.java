package renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import map.Loader;
import rays.Camera;
import scene.SceneManager;
import scene.Skybox;
import texture.ImageLoader;

public class Display extends Renderer {

	private static final long serialVersionUID = -6740323398587524531L;

	public static SceneManager manager = new SceneManager();
	
	private Loader map_loader = new Loader();
	
	private boolean up, down, left, right;
	
	private int FOV = 45;
	
	private float speed = 2;
	
	private Camera camera;

	public Skybox skybox = new Skybox(0,0, "res/skybox/skybox.jpg");
	
	public Display() {
		map_loader.loadMap("maps/test_map.lvl");
		map_loader.createMap();
		camera = new Camera(400, 400, 1080, 720, FOV);
		camera.setSpeed(speed);
		Random r = new Random();
		for(int i = 0; i < manager.walls.size(); i++) {
			if(r.nextInt() >= 6) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/textures/wolf/0" + (r.nextInt(8)+1) + ".BMP"));
			} else if(r.nextInt() >= 3) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/textures/wolf/0" + (r.nextInt(8)+1) + ".BMP"));
			} else {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/textures/wolf/0" + (r.nextInt(8)+1) + ".BMP"));
			}
		}
		repaint();
	} 

	public void update() {
		camera.update();
		camera.movement(isUp(), isDown(), isLeft(), isRight());
		if(isLeft()) {
			skybox.move(speed);
		} else if(isRight()) {
			skybox.move(-speed);
		} 
	}

	public void render(Graphics2D g) {
		skybox.render(this, g);
		manager.renderScene3D(g, this, camera);
		//manager.renderScene(g);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		render(g2);
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public int getFOV() {
		return FOV;
	}

	public void setFOV(int fOV) {
		FOV = fOV;
	}
	
}
