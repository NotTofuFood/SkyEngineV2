package rays;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import maths.ExtraMath;
import maths.Point;
import obj.ModelLoader;
import obj.Wall;
import scene.SceneManager;

public class Ray {
	
	private double x1, y1, x2, y2, x3, y3;
	private double degrees;
	private double distance;
	
	private boolean isPortal;
	
	private int wall_texture_id;
	private int entity_texture_id;
	
	private boolean hitHorizontal = false;
	private boolean hit_something = false;
	
	private double entity_distance;
	
	public boolean castingEntity = false;
	
	public Point gridPoint;

	public int index;
	
	private Wall wall;
	
	public List<Entity> entity;
	
	private double objectDistance = 0;
	
	
    public Ray(double x1, double y1, double degrees) {
        this.x1 = x1;
        this.y1 = y1; 
        this.degrees = degrees;
        this.x2 = this.x1 + Math.cos(this.degrees) * Integer.MAX_VALUE;
        this.y2 = this.y1 + Math.sin(this.degrees) * Integer.MAX_VALUE;
        entity = new ArrayList<>();
    }
    
    public void updateRotation() {
    	x2 += SceneManager.angles.cos[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    	y2 += SceneManager.angles.sin[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    }
    
    public void render(Graphics2D g, boolean render) {
    	if(render) {

    		if(castingEntity) { 
    			
    			g.setColor(Color.MAGENTA);
    		} else {
    		g.setColor(Color.RED);
    		}
    		g.draw(new Line2D.Double(x1, y1, x2, y2));
    	}
    	updateRotation();
    }
    
    // raycast normal. Save entity rays for the end and dont count the wall as an entity. Then see if the ray happened to pass any entities. Then store that value and render it like a wall.
    
    public void raycast(List<Wall> walls,int wall_index) {
    	Point check = ExtraMath.pointIntersection(x1, y1, x2, y2, walls.get(wall_index).getX1(), walls.get(wall_index).getY1(), walls.get(wall_index).getX2(), walls.get(wall_index).getY2());

        if(check != null) {
        	
        	x2 = check.getX();
        	y2 = check.getY();
        	
            wall = walls.get(wall_index);
            hitHorizontal = walls.get(wall_index).getWallType();
            double distance = ExtraMath.distance(x1, y1, x2, y2);
            isPortal = walls.get(wall_index).isPortal;
            wall_texture_id = wall_index;
            if(walls.get(wall_index).isEntity) entity_texture_id = 0;
            setDistance(distance);
        } else { 
        	hit_something = false;
        }
          
        
    }
    
    public void raycastEntity(Entity entity) {
    	
     //   double x2 = this.x1 + Math.cos(this.degrees) * Integer.MAX_VALUE;
    //    double y2 = this.y1 + Math.sin(this.degrees) * Integer.MAX_VALUE;
    	
      	Point check_entities =  ExtraMath.pointIntersection(x1, y1, x2, y2, entity.x,  entity.y, entity.x+entity.BoundingBoxSizeX,  entity.y + entity.BoundingBoxSizeY);
      	 if(check_entities != null) {
      
      		if(entity.clearFrame) { 
         	entity_distance = ExtraMath.distance(x1, y1, entity.x, entity.y);
         	entity.onScreen = true;
       
         	if(entity_distance < 400) {
         //	System.out.println("SPR: " + entity.sprite + " Dist: " + entity_distance + " Index: " + index + " Entity Frame: " + entity.clearFrame);
         	entity.rindex = index; 
         	entity.distance = entity_distance;
         	castingEntity = true;
         	entity.clearFrame = false;
         	} else {
         		entity.onScreen = false;
         		castingEntity = false;
         	}
      		}
         } 
    }
    
 

    
    public void raycastModel(List<ModelLoader> model, int index) {
        Point checkL = ExtraMath.pointIntersection(x1, y1, x2, y2, model.get(index).getPosition().getX(), model.get(index).getPosition().getY(), 
        		model.get(index).getPosition().getX(),
        		model.get(index).getPosition().getY()*model.get(index).getSize());
        
        Point checkR = ExtraMath.pointIntersection(x1, y1, x2, y2, model.get(index).getPosition().getX()*model.get(index).getSize(), model.get(index).getPosition().getY(), 
        		model.get(index).getPosition().getX()*model.get(index).getSize(),
        		model.get(index).getPosition().getY()*model.get(index).getSize());
        
        Point checkT = ExtraMath.pointIntersection(x1, y1, x2, y2, model.get(index).getPosition().getX(), model.get(index).getPosition().getY(), 
        		model.get(index).getPosition().getX()*model.get(index).getSize(),
        		model.get(index).getPosition().getY());
        
        Point checkB = ExtraMath.pointIntersection(x1, y1, x2, y2, model.get(index).getPosition().getX(), model.get(index).getPosition().getY()*model.get(index).getSize(), 
        		model.get(index).getPosition().getX()*model.get(index).getSize(),
        		model.get(index).getPosition().getY()*model.get(index).getSize());
        if(checkL != null) {
            double distance = ExtraMath.distance(x1, y1, checkL.getX(), checkL.getY());
            objectDistance = distance;
        } else if(checkR != null) {
            double distance = ExtraMath.distance(x1, y1, checkR.getX(), checkR.getY());
            objectDistance = distance;
        } else if(checkT != null) {
            double distance = ExtraMath.distance(x1, y1, checkT.getX(), checkT.getY());
            objectDistance = distance;
        } else if(checkB != null) {
            double distance = ExtraMath.distance(x1, y1, checkB.getX(), checkB.getY());
            objectDistance = distance;
        } else {
        	objectDistance = 0;
        }
    }
    
    public void portalcast(List<Wall> walls, int wall_index, int i) {
    	// x1 = px
    	// y1 = py
    	// x2 = px * cos
    	// y2 = py * sin
    	// portal x and portal y are your locations for the wall type, x and y are your destanation points
    	double px2 = 400;
    	double py2 = 400;
    	double px = 400;
    	double py = 400;
    	px2 += SceneManager.angles.cos[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    	py2 += SceneManager.angles.sin[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    	Point check = ExtraMath.pointIntersection(px, py, px2, py2, walls.get(i).getX1(), walls.get(i).getY1(), walls.get(i).getX2(), walls.get(i).getY2());
    	if(check != null)  {
    		hit_something = true;
    		x1 = px;
    		y1 = py;
    		x2 = check.getX();
    		y2 = check.getY();
    		wall = walls.get(i);
    		hitHorizontal = walls.get(i).getWallType();
    		double distance = ExtraMath.distance(x1, y1, x2, y2);
    		isPortal = walls.get(i).isPortal;
    		wall_texture_id = i;
    		setDistance(distance);
    	}
    
    }
    
    /*
     *    
    public void portalcast(List<Wall> walls, int wall_index) {
    	// x1 = px
    	// y1 = py
    	// x2 = px * cos
    	// y2 = py * sin
    	double px2 = walls.get(wall_index).getPortalID().getPortal_x();
    	double py2 = walls.get(wall_index).getPortalID().getPortal_y();
    	px2 += SceneManager.angles.cos[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    	py2 += SceneManager.angles.sin[(int) (Math.abs(Math.toDegrees(degrees)) * 100)] * Integer.MAX_VALUE;
    	for(int i = 0; i < walls.size(); i++) {
    	Point check = ExtraMath.pointIntersection(walls.get(wall_index).getPortalID().getPortal_x(), walls.get(wall_index).getPortalID().getPortal_y(), px2, py2, walls.get(i).getX1(), walls.get(i).getY1(), walls.get(i).getX2(), walls.get(i).getY2());
    	if(check != null)  {
    		hit_something = true;
    		x2 = check.getX();
    		y2 = check.getY();
    		x1 = px2;
    		y1 = py2;
    		System.out.println(y2);
    		wall = walls.get(wall_index);
    		// wall.getPortalID().getPortal_x()
    		hitHorizontal = walls.get(wall_index).getWallType();
    		double distance = ExtraMath.distance(x1, y1, x2, y2);
    		isPortal = walls.get(wall_index).isPortal;
    		wall_texture_id = wall_index;
    		setDistance(distance);
    	}
    	}
    }
     */
    
    public boolean getWallType() {
    	return hitHorizontal;
    }
    
	public boolean isCastingEntity() {
		return castingEntity;
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
	
	public int getEntityTextureID() {
		return entity_texture_id;
	}
	
	public boolean hitPortal() {
		return isPortal;
	}
	
	public boolean hitSomething() {
		return hit_something;
	}

	public Wall getWall() {
		return wall;
	}
	
	public double getObjectDistance() {
		return objectDistance;
	}

	public double getEntityDistance() {
		return entity_distance;
	}

	public Entity getEntity(int index) {
		return entity.get(index);
	}

	public void setEntity(Entity entity, int index) {
		this.entity.set(index, entity);
	}

	public double getX3() {
		return x3;
	}

	public void setX3(double x3) {
		this.x3 = x3;
	}
	
	public double getY3() {
		return y3;
	}

	public void setY3(double y3) {
		this.y3 = y3;
	}
	
}
