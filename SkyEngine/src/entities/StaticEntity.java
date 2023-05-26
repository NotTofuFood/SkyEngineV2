package entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.List;

import obj.Wall;
import rays.Ray;

public class StaticEntity extends Entity {

	public StaticEntity(String spr_name, int x, int y, int bbx, int bby) {
		this.sprite = spr_name;
		this.x = x;
		this.y = y;
		this.BoundingBoxSizeX = bbx;
		this.BoundingBoxSizeY = bby;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Rectangle2D other, List<Wall> wall_data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkCollisions(Rectangle2D other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g, ImageObserver io, Ray ray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntityToWallData(List<Wall> wall_data) {
		// TODO Auto-generated method stub
		
	}

}
