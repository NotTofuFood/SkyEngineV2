package rays;

import java.awt.geom.Line2D;

import java.util.List;

import obj.Wall;
import renderer.Display;

public class Camera  {

	private int x;
	private int y;
	
	private int FOV;
	
	private float rotation = 0.0f;
	
	private float speed = 0.0f;
	
	private float hsp = 0.0f;
	private float vsp = 0.0f;
	
	private double sensitivity = 1.5;
	
	private double multiplier = 0.8;
	
	private boolean stop_up, stop_down;
	
	public boolean move_rot = false;
	
	public Camera(int x, int y, int WIDTH, int HEIGHT, int FOV) {
		super();
		this.x = x;
		this.y = y;
		this.FOV = FOV;
		init();
	}
	
	private void init() {
		for(int i = 0; i < Display.manager.projection_width; i++) {
			Display.manager.rays.add(new Ray(x, y, Math.toRadians(i*FOV*multiplier/Display.manager.projection_width)));
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
		if(left) {
			move_rot = true;
			moveRotation(-sensitivity);
		}
		else if(right) {
			move_rot = true;
			moveRotation(sensitivity);
		} else {
			move_rot = false;
			moveRotation(0);
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
		rot = Display.manager.rays.get(Display.manager.projection_width/FOV).getDegrees();
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