package renderer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import audio.AudioManager;
import entities.Entity;
import entities.NPC;
import entities.StaticEntity;
import gui.CustomParameter;
import gui.GUI_Segment;
import gui.ParametersValues;
import input.Input;
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
	
	public static int FOV = 64; //128 75 //11.38 //150
	
	private float speed = 4;
	
	private Portal test_portal;
	
	private Camera camera;

	private Skybox skybox;
	
	private VolatileImage screen;
	
	public static String current_map;
	
	public Light test_light;
	
	public GUI_Segment gui_test;
	public GUI_Segment gun_screen;
	
	public List<CustomParameter> info = new ArrayList<>();
	
	public List<CustomParameter> gun = new ArrayList<>();
	
	public NPC test_npc = new NPC(200, 200, 64, 128, 5);
	
	public List<Entity> entites = new ArrayList<>();
	
	public CustomParameter reload = new CustomParameter(ParametersValues.GUI_IMAGE, "/res/fps/beretta_standing.png", (int)(Window.WIDTH/2)-250, (int)(Window.HEIGHT/2)-200, 640, 368, true);
	public CustomParameter idle = new CustomParameter(ParametersValues.GUI_IMAGE, "/res/fps/beretta_standing.png",  55, 64, 1, 1, true);
	public CustomParameter fire = new CustomParameter(ParametersValues.GUI_IMAGE, "/res/fps/beretta_fire.png", 48, 64, 1, 1, true);
	
	public StaticEntity box = new StaticEntity("res/hecu.png", 103, 183, 1, 1);
	
	public TimerTask fire_test;
	
	public boolean canFire = true;
	
	public Display() {
		
		gun.add(reload);
		info.add(new CustomParameter(ParametersValues.GUI_TEXT, "MISSION: DEMO OPS.........WEAPON: M9 A1", 10, 20));
		info.add(new CustomParameter(ParametersValues.GUI_TEXT, "SkyEngine V.2.4 - Aiden Thakurdial", 10, 40));
		info.add(new CustomParameter(ParametersValues.GUI_TEXT, "+", (int)(Window.WIDTH/2), (int)(Window.HEIGHT/2)));

		gun_screen = new GUI_Segment(gun);
		
		gui_test = new GUI_Segment(info);
		gui_test.text_font = new Font("Helvetica", Font.BOLD, 9);
		
		
		skybox = new Skybox(0,0, "res/skybox/skybox.png");
		skybox.manager = manager;
		map_loader.loadMap("maps/demomap2.lvl");
		map_loader.createMap(); 
		camera = new Camera(200, 202, FOV);
		camera.setSpeed(speed);
		Random r = new Random();
		manager.fog_settings(new ThreeValue(0, 0, 0), 3);
		
		for(int i = 0; i < manager.walls.size(); i++) {
			if(r.nextInt() >= 7) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/tex/brickwall_1.png", manager.walls.get(i)));
			} else if(r.nextInt() >= 5) {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/tex/brickwall_2.png",manager.walls.get(i)));
			} else {
				manager.walls.get(i).setTexture(ImageLoader.loadImage("res/tex/brickwall_3.png", manager.walls.get(i)));
			} 

		}
		
		for(int light = 0; light < 5; light++) {
			test_light = new Light(r.nextInt(800),r.nextInt(800), 100, new ThreeValue(r.nextInt(255),r.nextInt(255),r.nextInt(255))); //228,140,242
			//manager.lights.add(new Light(r.nextInt(800),r.nextInt(800),100,new ThreeValue(228, 140, 242)));
			//manager.lights.add(test_light);
		}
		manager.image_size = ImageLoader.wall_textures.size();
		
		// portal x and portal y are your locations for the wall type, x and y are your destanation points
		//test_portal = new Portal(400, 400, 100, 100, 0, (int) Window.WIDTH, (int) Window.HEIGHT);
	//	manager.createPortal(test_portal, this);

		//manager.floor_image = ImageLoader.loadFloorTileset("res/half life.png", 16*1, 16*10, 32);
		//manager.floor_image = ImageLoader.loadFloorImage("res/wood.png");
		//manager.ceiling_image = ImageLoader.loadFloorTileset("res/half life.png", 16*10, 16*4, 32);
		
		manager.floor_image = ImageLoader.loadFloorImage("res/tex/stonefloor_1.png");
	//	manager.ceiling_image = ImageLoader.loadFloorImage("res/skybox/skybox.png");
	//	manager.disableceil = true;
		
		manager.disableceil = false;
		manager.ceiling_image = ImageLoader.loadFloorImage("res/tex/tilefloor_3.png");
		
		manager.safe_init();
		
		//ImageLoader.loadEntityImage(box.sprite, box);
		entites.add(box);
		
		for(int i = 0; i < 20; i++) {
			int x = (int) Math.round(r.nextInt(900));
			int y = (int) Math.round(r.nextInt(900));
			
			Random re = new Random();
			StaticEntity box;
			if(re.nextInt(20) > 15) {
				 box = new StaticEntity("res/hecu.png", x, y, 4, 4);
			} else {
				 box = new StaticEntity("res/textures/wolf/barrel.png", x, y, 4, 4);
			}
			entites.add(box);
		}
		
		gun.set(0, idle);
		
		repaint();
	} 
	
	public void update() {
		
		for(int guis = 0; guis < gun.size(); guis++) {
			gun.get(guis).updateBob(camera);
		}
		
		camera.movement(isUp(), isDown(), isLeft(), isRight());
		camera.updateMouse(manager);
		camera.update();
		manager.sortEnts(entites, camera, new double[entites.size()-1]);
		if(camera.getRotationMovement() && Input.MouseLeft() && !Input.MouseRight()) {
			skybox.move(-speed);
		} else if(camera.getRotationMovement() && !Input.MouseLeft() && Input.MouseRight()) {
			skybox.move(speed);
		} 
		
		if(Input.clicked && canFire) {
			AudioManager.playAudio("res/audio/fire.wav");
			gun.set(0, fire);
			System.out.println(Math.toRadians(camera.getRotation()));
			
			int light_x = (int) (100*Math.cos(Math.toRadians(camera.getRotation())));
			int light_y = (int) (100*Math.sin(Math.toRadians(camera.getRotation())));
			
			manager.lights.add(new Light(camera.getX()+light_x, camera.getY() + light_y, 150,new ThreeValue(27, 14, 0)));
			Timer time = new Timer();
			int delay = 50; // 1 Sec
			canFire = false;
			Input.clicked = false;
			fire_test = new TimerTask() {
				public void run() {
					canFire = true;
					manager.lights.remove(manager.lights.size()-1);
					gun.set(0, idle);
				}
			};
			time.schedule(fire_test, delay);
		} else if(!Input.clicked) {
			//gun.set(0, idle);
		}
		
		if(Input.getReload()) {
			gun.set(0, reload);
		}
		
		
	}

	public void render(Graphics2D g) {
		skybox.render(this, g);
		manager.renderScene3D(g, this, camera, skybox, entites);

		manager.renderEntities(entites, g, this);
		
		gun_screen.segment_render(g, this);
		gui_test.segment_render(g, this);


	//	manager.renderScene(g, entites);

	//	manager.renderInfo(g, entites, camera);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(Window.ACTUAL_WIDTH/Window.WIDTH, Window.ACTUAL_HEIGHT/Window.HEIGHT);
		screen = this.getGraphicsConfiguration().createCompatibleVolatileImage((int)Window.WIDTH, (int)Window.HEIGHT);
		//manager.floor_buffer = this.getGraphicsConfiguration().createCompatibleVolatileImage((int)Window.WIDTH, (int)manager.floor_height);
		render(screen.createGraphics());
		g2.drawImage(screen, 0, 0, this);
		/*
		if(camera.move_rot) {
			for(int i = 0; i < 0; i++) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)i/500));
				g2.drawImage(screen, i*(int)manager.wall_width, 0, this);
			}
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}*/
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
