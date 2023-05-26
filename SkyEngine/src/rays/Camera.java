package rays;

import java.awt.geom.Line2D;
import java.util.List;

import input.Input;
import main.Window;
import obj.Wall;
import renderer.Display;
import scene.SceneManager;

public class Camera  {

	private int x;
	private int y;
	
	private int FOV;
	
	private float rotation = 0.0f;
	
	private float speed = 0.0f;
	
	private float hsp = 0.0f;
	private float vsp = 0.0f;
	
	public double sensitivity = 1.5;
	
	public static double multiplier = 0.8;
	
	public boolean stop_up, stop_down;
	
	public boolean move_rot = false;
	
	private int headbob = 0;
	
	public double[] headbobs = new double[360];
	
	public int current_head = 0;
	
	public Camera(int x, int y, int FOV) {
		super();
		this.x = x;
		this.y = y;
		this.FOV = FOV;
		init();
	}
	
	private void init() {
		for(int i = 0; i < Display.manager.projection_width; i++) {
			Display.manager.rays.add(new Ray(x, y, Math.toRadians(i*FOV*multiplier/Display.manager.projection_width)));
			Display.manager.rays.get(i).index = i;
		}
		
		for(int i = 0; i < 360; i++) {
			headbob = (headbob+(int)4)%360;

			headbobs[i] = Math.sin(Math.toRadians(headbob))*0.3;
		}
		
	}
	
	public void checkCollisions(List<Wall> walls, int wall_index) {
		Line2D camera = new Line2D.Double(x,y,x,y-20);
		Line2D wall = new Line2D.Double(walls.get(wall_index).getX1(),walls.get(wall_index).getY1(),walls.get(wall_index).getX2(), walls.get(wall_index).getY2());
		
		if(camera.intersectsLine(wall)) {
		//	vsp+=speed;
		} else {
		//	stop_up = false;
		}
		
	}
	
	public void updateMouse(SceneManager manager) {
		if(Input.MouseLeft() && !Input.MouseRight()) {
			moveRotation(-sensitivity);
			move_rot = true;
		}
		else if(Input.MouseRight() && !Input.MouseLeft()) {
			moveRotation(sensitivity);
			move_rot = true;
		} else {
			move_rot = false;
		}
	/*	if(Input.MouseUp() && !Input.MouseDown()) {
			manager.camera_height+=sensitivity;
		}
		if(!Input.MouseUp() && Input.MouseDown()) {
			manager.camera_height-=sensitivity;
		}*/
		
		if(hsp != 0) {
			current_head++;
			current_head%=360;
			manager.camera_height+=headbobs[current_head];
		} else {
			manager.camera_height = Window.HEIGHT/2;
		}
		
		Input.resetMouse();
	}
	
	public boolean getRotationMovement() {
		return move_rot;
	}
	
	public void movement(boolean up, boolean down, boolean left, boolean right) { 
		if(up && !stop_up) {
			moveForward(speed);
		}
		else if(down && !stop_down) {
			moveForward(-speed);
		} else {
			hsp = 0;
			vsp = 0;
		}

		if(Math.abs(rotation) > 360) {
			rotation = 0;
		}
		if(rotation < 0) {
			rotation = 360;
		}
		
		x+=hsp;
		y+=vsp;
	}
	
	public void update() {
		for(int i = 0; i < Display.manager.projection_width; i++) {
			Display.manager.rays.get(i).setX1(x);
			Display.manager.rays.get(i).setY1(y);
		}
	}
	
	private void moveForward(float speed) {
		double rot = 0.0;
		rot = Display.manager.rays.get(Display.manager.projection_width/2).getDegrees();
		hsp = (float) (Math.cos(rot) * speed);
		vsp = (float) (Math.sin(rot) * speed);
	}
	
	private void moveRotation(double speed) {
		rotation+=speed;
		for(int i = 0; i < Display.manager.projection_width; i++) {
			Display.manager.rays.get(i).setDegrees(Math.toRadians(rotation + i*FOV*multiplier/Display.manager.projection_width));
		}
	}
	
	public double getRotation() {
		return rotation;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setHSP(float speed) {
		this.hsp = speed;
	}
	
	public void setVSP(float speed) {
		this.vsp = speed;
	}

	public float getHSP() {
		return hsp;
	}
	
	public float getVSP() {
		return vsp;
	}
	
	
	public int getFOV() {
		return FOV;
	}

	public void setFOV(int fOV) {
		FOV = fOV;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

}