package obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import rays.Portal;

public class Wall {
	
	private double x1, y1, x2, y2;
	private BufferedImage texture;

	public boolean isPortal = false;
	
	public Portal portal_id;
	
	public int textureID = 1;
	
    public Wall(double x1, double y1, double x2, double y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void render(Graphics2D g) {
    	g.setColor(Color.WHITE);
    	g.draw(new Line2D.Double(x1, y1, x2, y2));
    }

	public boolean getWallType() {
		if(Math.abs(x1-x2) > Math.abs(y1-y2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Portal getPortalID() {
		return portal_id;
	}
	
	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
}
