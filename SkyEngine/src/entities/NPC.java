package entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import maths.ExtraMath;
import obj.Wall;
import rays.Ray;
import texture.ImageLoader;

public class NPC extends Entity {
	
	//For random movement only
	public Random r;
	
	public NPC(int x, int y, int bbx, int bby, int sp) {
		start();
		this.x = x;
		this.y = y;
		this.sp = sp;
		this.BoundingBoxSizeX=bbx;
		this.BoundingBoxSizeY=bby;
		this.worldspace = new Wall(this.x, this.y, this.x+bbx, this.y+bby);
		BoundingBox = new Rectangle2D.Float(x,y,bbx,bby);
		sprite = "res/Aiden.png";
		r = new Random();
		this.worldspace.isEntity = true;
	}
	
	public void start() {
	
	}

	public void checkCollisions(Rectangle2D other) {
		if(other.intersects(BoundingBox)) {
			hsp = 0;
			vsp = 0;
		}
	}
	
	public void update(Rectangle2D other, List<Wall> wall_data) {
		
		move();
		if(other != null) {
			checkCollisions(other);
		}
		x+=hsp;
		y+=vsp;
		
		wall_data.get(world_index).setX1(this.x);
		wall_data.get(world_index).setY1(this.y);
		wall_data.get(world_index).setX2(this.x + BoundingBox.getWidth());
		wall_data.get(world_index).setY2(this.y + BoundingBox.getHeight());
	}

	public void move() {
		if(canMove) {
			//Random Movement Test
			if(r.nextInt(30) == 3) {
				hsp = ExtraMath.choose(1, 0, -1, r) * sp;
			} else if(r.nextInt(30) == 7) {
				vsp = ExtraMath.choose(1, 0, -1, r) * sp;
			} else {
				hsp = 0;
				vsp = 0;
			}
				
		}
	}

	public void render(Graphics2D g, ImageObserver io, Ray ray) {
		//g.drawImage(sprite, ray.getX1(), ray.getY1(), io);
	}

	public void interact() {

	}

	public void addEntityToWallData(List<Wall> wall_data) {
		world_index = wall_data.size()-1;
		wall_data.get(world_index).isEntity = true;
		wall_data.get(world_index).entity = this;
	}



}
