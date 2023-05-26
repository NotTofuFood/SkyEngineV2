package entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import obj.Wall;
import rays.Ray;

public abstract class Entity {
	
	public int x = 0;
	public int y = 0;
	
	public int z = 0;
	
	public float sp = 0.0f;
	public float hsp = 0.0f;
	public float vsp = 0.0f;
	
	public int BoundingBoxSizeX = 0;
	public int BoundingBoxSizeY = 0;
	
	public double distance;
	public int rindex;
	public int index;
	public boolean isVisible = false;
	
	public boolean canMove = false;
	public boolean canInteract  = false;
	
	public String sprite;
	public List<BufferedImage> sprites = new ArrayList<>();
	
	public Rectangle2D BoundingBox;
	
	public boolean clearFrame = false;
	public boolean onScreen = false;
	
	public Wall worldspace;
	
	protected int world_index;
	
	public abstract void start();
	public abstract void update(Rectangle2D other, List<Wall> wall_data);
	public abstract void checkCollisions(Rectangle2D other);
	public abstract void move();
	public abstract void render(Graphics2D g, ImageObserver io, Ray ray);
	public abstract void interact();
	public abstract void addEntityToWallData(List<Wall> wall_data);
}
