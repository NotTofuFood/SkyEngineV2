package scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import main.Window;
import maths.ExtraMath;
import obj.Wall;
import rays.Camera;
import rays.Ray;

public class SceneManager {
	
	public List<Wall> walls = new ArrayList<>();
	public List<Ray> rays = new ArrayList<>();
	
	private final double wall_width = 64;
	private final double wall_height = Window.HEIGHT;
	
	public void renderScene(Graphics2D g) {
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int wall = 0; wall < walls.size(); wall++) {
				walls.get(wall).render(g);
				rays.get(ray).raycast(walls, wall);
			}
			rays.get(ray).render(g, true);
		}
	}
	
	public void renderSceneUnTextured3D(Graphics2D g, Camera c) {
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int wall = 0; wall < walls.size(); wall++) {
				rays.get(ray).raycast(walls, wall);
			}
			double height = wall_width * wall_height / rays.get(ray).getDistance() * Math.cos(Math.toRadians(c.getRotation()));
			double height_offset = (Window.HEIGHT/4)-height/4;
			int color = (int) ExtraMath.clamp(255 - rays.get(ray).getDistance()/2, 0, 255);
			g.setColor(new Color(color, color, color));
			if(height > wall_height) 
				height = wall_height;
			if(height < 1) 
				height = 0;
			g.fillRect(ray*(int)wall_width, (int)height_offset, (int)wall_width, (int)Math.abs(height+height_offset));
			rays.get(ray).render(g, false);
		}
	}
	
	 public void renderScene3D(Graphics2D g, ImageObserver io, Camera c) {
		g.setColor(Color.black);
	    g.fillRect(0, (int)Window.HEIGHT/2, (int)Window.WIDTH, (int)Window.HEIGHT/2);
		for(int ray = 0; ray < rays.size(); ray++) {
			for(int wall = 0; wall < walls.size(); wall++) {
				rays.get(ray).raycast(walls, wall);
			}
			double height = wall_width * wall_height / rays.get(ray).getDistance() * Math.abs(Math.cos(Math.toRadians(c.getRotation())));
			double height_offset = (Window.HEIGHT/4)-height/4;
			int color = (int) ExtraMath.clamp(255 - rays.get(ray).getDistance(), 0, 255);
			if(height > wall_height) 
				height = wall_height;
			if(height < 1)  
				height = 0;
			int x_offset = (int)Math.abs(rays.get(ray).getXOffset(wall_width)) - (int)Math.floor(Math.abs(rays.get(ray).getXOffset(wall_width)));
			int size = (int)walls.get(rays.get(ray).getTextureID()).getTexture().getWidth();
			//g.setXORMode(new Color(color, color, color, 100)); //PBR
			g.setColor(new Color(color, color, color, 120));
			g.drawImage(walls.get(rays.get(ray).getTextureID()).getTexture().getSubimage(x_offset, 0, size, walls.get(rays.get(ray).getTextureID()).getTexture().getHeight()),
					ray*(int)wall_width, (int)height_offset, (int)wall_width, (int)Math.abs(height+height_offset), io);
			g.fillRect(ray*(int)wall_width, (int)height_offset, (int)wall_width, (int)Math.abs(height+height_offset));
			rays.get(ray).render(g, false);
		}
	}


}
