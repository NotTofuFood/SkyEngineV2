package rays;

import java.util.ArrayList;
import java.util.List;

import renderer.Display;

public class Portal {

	private int x;
	private int y;
	
	private int portal_x;
	private int portal_y;
	
	private int FOV = 45;
	
	private float rotation = 0.0f;
	
	private float speed = 0.0f;
	
	public List<Ray> portal_rays = new ArrayList<>();
	
	public Portal(int x, int y, int portal_x, int portal_y, int rotation, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.portal_x = portal_x;
		this.portal_y = portal_y;
		this.rotation = rotation;
		init();
	}
	
	private void init() {

	}
	
	public void moveRotation(double speed) {

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

	public int getPortal_x() {
		return portal_x;
	}

	public void setPortal_x(int portal_x) {
		this.portal_x = portal_x;
	}

	public int getPortal_y() {
		return portal_y;
	}

	public void setPortal_y(int portal_y) {
		this.portal_y = portal_y;
	}

}