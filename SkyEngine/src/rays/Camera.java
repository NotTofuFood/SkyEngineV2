package rays;

import renderer.Display;

public class Camera  {

	private int x;
	private int y;
	
	private int FOV;
	
	private float rotation = 0.0f;
	
	private float speed = 0.0f;
	
	public Camera(int x, int y, int WIDTH, int HEIGHT, int FOV) {
		super();
		this.x = x;
		this.y = y;
		this.FOV = FOV;
		init();
	}
	
	private void init() {
		for(int i = 0; i < FOV; i++) {
			Display.manager.rays.add(new Ray(x, y, FOV, Math.toRadians(i)));
		}
	}
	
	public void movement(boolean up, boolean down, boolean left, boolean right) { 
		if(up) {
			moveForward(speed);
		}
		if(down) {
			moveBackward(speed);
		}
		if(left) {
			moveRotation(-speed);
		}
		else if(right) {
			moveRotation(speed);
		} else {
			moveRotation(0);
		}
	}
	
	public void update() {
		for(int i = 0; i < FOV; i++) {
			Display.manager.rays.get(i).setX1(x);
			Display.manager.rays.get(i).setY1(y);
		}
	}
	
	public void moveForward(float speed) {
		double rot = 0.0;
		for(int i = 0; i < FOV; i++) {
			if(i == FOV/2) {
				rot = Display.manager.rays.get(i).getDegrees();
				break;
			}
		}
    	x += Math.cos(rot) * speed;
    	y += Math.sin(rot) * speed;
	}
	public void moveBackward(float speed) {
		double rot = 0.0;
		for(int i = 0; i < FOV; i++) {
			if(i == FOV/2) {
				rot = Display.manager.rays.get(i).getDegrees();
				break;
			}
		}
    	x -= Math.cos(rot) * speed;
    	y -= Math.sin(rot) * speed;
	}
	
	public void moveRotation(double speed) {
		rotation+=speed;
		for(int i = 0; i < FOV; i++) {
			Display.manager.rays.get(i).setDegrees(Math.toRadians(rotation + i));
		}
	}
	
	public void resetRotation() {
		for(int i = 0; i < FOV; i++) {
			Display.manager.rays.get(i).setDegrees(Math.toRadians(i));
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
