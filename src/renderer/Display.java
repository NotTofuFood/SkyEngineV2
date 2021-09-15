package renderer;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.Random;

import lighting.Light;
import main.Window;
import map.Loader;
import maths.ThreeValue;
import rays.Camera;
import rays.Portal;
import scene.SceneManager;
import scene.Skybox;
import texture.ImageLoader;

public class Display extends Renderer {

	private static final long serialVersionUID = -6740323398587524531L;

	public static SceneManager manager = new SceneManager(4); //6
	
	private Loader map_loader = new Loader();
	
	private boolean up, down, left, right;
	
	public static int FOV = 75; //128
	
	private float speed = 4;
	
	private Portal test_portal;
	
	private Camera camera;

	private Skybox skybox;
	
	private VolatileImage screen;
	
	public static String current_map;
	
	public Light test_light;
	
	public Display() {
		skybox = new Skybox(0,0, "res/skybox/space.jpg");
		map_loader.loadMap("maps/open.lvl");
		map_loader.createMap(); 
		camera = new Camera(400, 432, FOV);
		camera.setSpeed(speed);
		Random r = new Random();
		manager.fog_settings(new ThreeValue(0, 0, 0), 4);
		
		for(int i = 0; i < manager.walls.size(); i++) {
//			if(r.nextInt() >= 7) {
//				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/brick_COLOR.png", manager.walls.get(i)));
//			} else if(r.nextInt() >= 5) {
//				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/aiden.png",manager.walls.get(i)));
//			} else {
//				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/brc.png", manager.walls.get(i)));
//			}
			//manager.walls.get(i).setTexture(ImageLoader.loadImage("res/aiden.png", "res/aiden_height.png", manager.walls.get(i)));
			//manager.walls.get(i).setTexture(ImageLoader.loadImage("res/checker.png", manager.walls.get(i)));
			if(r.nextInt() >= 7) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/brickwall.png", manager.walls.get(i)));
			} else if(r.nextInt() >= 5) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/doomtexture.png", manager.walls.get(i)));
			} else {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/planks.png", manager.walls.get(i)));
			}
		}
		
		for(int light = 0; light < 4; light++) {
			test_light = new Light(r.nextInt(300),r.nextInt(300), 10, new ThreeValue(r.nextInt(255),r.nextInt(255),r.nextInt(255))); //228,140,242
			manager.lights.add(test_light);
		}
		manager.image_size = ImageLoader.wall_textures.size();
		
		// portal x and portal y are your locations for the wall type, x and y are your destanation points
		test_portal = new Portal(400, 400, 100, 100, 0, (int) Window.WIDTH, (int) Window.HEIGHT);
	//	manager.createPortal(test_portal, this);

		//manager.floor_image = ImageLoader.loadFloorTileset("res/half life.png", 16*1, 16*10, 32);
		manager.floor_image = ImageLoader.loadFloorImage("res/wood.png");
		manager.ceiling_image = ImageLoader.loadFloorTileset("res/half life.png", 16*10, 16*4, 32);
		
		manager.safe_init();
		repaint();
	} 
	
	public void update() {
		camera.movement(isUp(), isDown(), isLeft(), isRight());
		camera.updateMouse(manager);
		camera.update();
		test_portal.setX(400);
		test_portal.setY(650);
		if(isLeft()) {
			skybox.move(speed);
		} else if(isRight()) {
			skybox.move(-speed);
		} 
	}

	public void render(Graphics2D g) {
		//skybox.render(this, g);
		manager.renderScene3D(g, this, camera, skybox);
		//manager.renderScene(g);
		//manager.renderSceneUnTextured3D(g, camera);
		//manager.renderInfo(g, camera);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(Window.ACTUAL_WIDTH/Window.WIDTH, Window.ACTUAL_HEIGHT/Window.HEIGHT);
		screen = this.getGraphicsConfiguration().createCompatibleVolatileImage((int)Window.WIDTH, (int)Window.HEIGHT);
		//manager.floor_buffer = this.getGraphicsConfiguration().createCompatibleVolatileImage((int)Window.WIDTH, (int)manager.floor_height);
		render(screen.createGraphics());
		g2.drawImage(screen, 0, 0, this);
		if(camera.move_rot) {
			for(int i = 0; i < 0; i++) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)i/500));
				g2.drawImage(screen, i*(int)manager.wall_width, 0, this);
			}
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
		g2.dispose();
		screen.flush();
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
