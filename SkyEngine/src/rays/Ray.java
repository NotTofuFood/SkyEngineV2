package rays;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

import maths.Point;
import maths.ExtraMath;
import obj.Wall;

public class Ray {
	
	private double x1, y1, x2, y2;
	private double degrees;
	private double distance;
	
	private boolean isPortal;
	
	private int wall_texture_id;
	
	private boolean hitHorizontal = false;
	
	private Wall wall;
	
    public Ray(double x1, double y1, double degrees) {
        this.x1  = x1;
        this.y1 = y1; 
        this.x2 = this.x1 + Math.cos(degrees) * Integer.MAX_VALUE;
        this.y2 = this.y1 + Math.sin(degrees) * Integer.MAX_VALUE;
        this.degrees = degrees;
    }
    
    public void updateRotation() {
    	x2 += Math.cos(degrees) * Integer.MAX_VALUE;
    	y2 += Math.sin(degrees) * Integer.MAX_VALUE;
    }
    
    public void render(Graphics2D g, boolean render) {
    	if(render) {
    		g.setColor(Color.RED);
    		g.draw(new Line2D.Double(x1, y1, x2, y2));
    	}
    	updateRotation();
    }
    
    public void raycast(List<Wall> walls, int wall_index) {
        Point check = ExtraMath.pointIntersection(x1, y1, x2, y2, walls.get(wall_index).getX1(), walls.get(wall_index).getY1(), walls.get(wall_index).getX2(), walls.get(wall_index).getY2());
        if(check != null) {
            x2 = check.getX();
            y2 = check.getY();
            wall = walls.get(wall_index);
            hitHorizontal = walls.get(wall_index).getWallType();
            double distance = ExtraMath.distance(x1, y1, check.getX(), check.getY());
            isPortal = walls.get(wall_index).isPortal;
            wall_texture_id = wall_index;
            setDistance(distance);
        }
    }
    
    public boolean getWallType() {
    	return hitHorizontal;
    }
    
    private void setDistance(double distance) {
    	this.distance = distance;
    }
    
    public double getDistance() {
    	return distance;
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

	public double getDegrees() {
		return degrees;
	}

	public void setDegrees(double degrees) {
		this.degrees = degrees;
	}
	
	public void addDegrees(double degrees) {
		this.degrees+=degrees;
	}
	
	public double getXOffset(double wall_width) {
		return x2 % wall_width;
	}
	
	public double getYOffset(double wall_height) {
		return y2 % wall_height;
	}

	public double getX2() {
		return x2;
	}
	
	public double getY2() {
		return y2;
	}
	
	public double getOffset() {
		return x2 % y2;
	}
	
	public int getTextureID() {
		return wall_texture_id;
	}
	
	public boolean hitPortal() {
		return isPortal;
	}

	public Wall getWall() {
		return wall;
	}
	
}
